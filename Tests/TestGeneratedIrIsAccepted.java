package Tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGeneratedIrIsAccepted {

    private static final String LLVM_AS = findTool("llvm-as");

    private void assertLlvmAccepts(final String source) throws Exception {
        final String module = ZFixtures.module(source);
        assertNotNull(module, "the module did not generate");

        if (LLVM_AS == null) return;

        final java.nio.file.Path source_file = java.nio.file.Files.createTempFile("zcheck", ".ll");
        final java.nio.file.Path object = java.nio.file.Files.createTempFile("zcheck", ".bc");
        try {
            java.nio.file.Files.writeString(source_file, module);
            final Process process = new ProcessBuilder(
                    LLVM_AS, source_file.toString(), "-o", object.toString())
                    .redirectErrorStream(true)
                    .start();
            final String diagnostics = new String(process.getInputStream().readAllBytes());
            assertTrue(process.waitFor() == 0, "llvm-as rejected the module:\n" + diagnostics + module);
        } finally {
            java.nio.file.Files.deleteIfExists(source_file);
            java.nio.file.Files.deleteIfExists(object);
        }
    }

    private static String findTool(final String name) {
        final String path = System.getenv("PATH");
        if (path == null) return null;
        for (final String directory : path.split(":")) {
            final java.nio.file.Path candidate = java.nio.file.Path.of(directory, name);
            if (java.nio.file.Files.isExecutable(candidate)) return candidate.toString();
        }
        return null;
    }

    @Test
    public void arithmeticIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() { ret 1 + 2 * 3 - 4 }");
    }

    @Test
    public void floatingArithmeticIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() { ret 1.5 + 2.5 }");
    }

    @Test
    public void unaryMinusOnFloatIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() { ret -5.5 }");
    }

    @Test
    public void comparisonIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() { ret 3 < 4 }");
    }

    @Test
    public void comparisonBetweenFloatsIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() { ret 1.5 < 2.5 }");
    }

    @Test
    public void comparisonInAForHeaderIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() {\n for (flt64 i -> 0.0 | i < 2.0 | i + 0.5) { }\n ret 0\n}");
    }

    @Test
    public void forHeaderComparingAnIntegerAgainstAFloatIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() {\n for (int32 i -> 0 | i < 2.5 | i + 1) { }\n ret 0\n}");
    }

    @Test
    public void int64ArithmeticIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() { int64 a -> 2\n int64 b -> 3\n ret a + b }");
    }

    @Test
    public void mixedWidthsAreAccepted() throws Exception {
        assertLlvmAccepts("proc main() { ret 1 + 2.5 }");
    }

    @Test
    public void floatVariableIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() { flt32 a -> 1.5\n ret a }");
    }

    @Test
    public void argumentConversionIsAccepted() throws Exception {
        assertLlvmAccepts("proc half(flt64 x) { ret x / 2 }\nproc main() { ret half(21) }");
    }

    @Test
    public void constantDeclarationIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() { cn int32 k -> 2\n ret k }");
    }

    @Test
    public void mutuallyRecursiveCallsAreAccepted() throws Exception {
        assertLlvmAccepts("proc a(int32 n) { ret b(n) }\nproc b(int32 n) { ret a(n) }\nproc main() { ret a(1) }");
    }

    @Test
    public void variablesAreAccepted() throws Exception {
        assertLlvmAccepts("proc main() { int32 a -> 1\n flt64 b -> 2.5\n ret a }");
    }

    @Test
    public void constantsAreAccepted() throws Exception {
        assertLlvmAccepts("proc main() { cn int32 a -> 1\n cn flt64 b -> 2.5\n ret a }");
    }

    @Test
    public void forwardCallIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() { ret twice(21) }\nproc twice(int32 x) { ret x * 2 }");
    }

    @Test
    public void recursiveCallIsAccepted() throws Exception {
        assertLlvmAccepts("proc fact(int32 n) { ret n * fact(n - 1) }\nproc main() { ret fact(5) }");
    }

    @Test
    public void callWithArgumentsIsAccepted() throws Exception {
        assertLlvmAccepts("proc add(int32 x | int32 y) { ret x + y }\nproc main() { ret add(1 | 2) }");
    }

    @Test
    public void nestedCallIsAccepted() throws Exception {
        assertLlvmAccepts("proc a(int32 n) { ret n + 1 }\nproc b(int32 n) { ret a(a(n)) }\nproc main() { ret b(1) }");
    }

    @Test
    public void forLoopIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() {\n for (int32 i -> 0 | i < 10 | i + 1) { }\n ret 0\n}");
    }

    @Test
    public void floatForLoopIsAccepted() throws Exception {
        assertLlvmAccepts("proc main() {\n for (flt64 i -> 0.0 | i < 2.0 | i + 0.5) { }\n ret 0\n}");
    }

    @Test
    public void returnInsideForLoopIsAccepted() throws Exception {
        assertLlvmAccepts("proc find(int32 n) {\n for (int32 i -> 0 | i < n | i + 1) {\n ret i\n }\n ret 0\n}");
    }

    @Test
    public void wideIntegersAreAccepted() throws Exception {
        assertLlvmAccepts("proc main() { int64 big -> 9007199254740993\n ret big }");
    }

    @Test
    public void severalFunctionsInOneFileAreAccepted() throws Exception {
        assertLlvmAccepts("proc a() { ret 1 }\nproc b() { ret a() }\nproc main() { ret b() }");
    }
}
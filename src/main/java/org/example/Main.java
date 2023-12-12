package org.example;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.io.IOAccess;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        final Context.Builder graal = Context.newBuilder("python", "llvm")
                .option("python.Executable", "venv/bin/graalpython")
                .option("python.ForceImportSite", "true")
                .allowIO(IOAccess.ALL)
                .allowNativeAccess(true);
        try (Context ctx = graal.build()) {
            // First, import the required packages
            ctx.eval("python", """
                    from beancount import loader
                    from beancount.core.data import Commodity
                    """);
            // Then, load the ledger
            ctx.eval("python", "(entries, errors, option_map) = loader.load_file('test.beancount')");
            // Find all symbols
            final Value pySymbols = ctx.eval("python", "[entry.currency for entry in entries if isinstance(entry, Commodity)]");
            // Convert them to their Java representation
            final String[] symbols = pySymbols.as(String[].class);
            System.out.println(Arrays.toString(symbols));
        }
    }
}

/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Duy on 8/30/2017.
 */

public class GenerateTest extends TestCase {
    public void testGenerateSystem() throws IOException {
    }

    public void testGenerateIfElse() throws IOException {
        String className = "IfElseTest";
        String outPath = System.getProperty("user.dir")
                + "\\app\\src\\test\\java\\com\\duy\\pascal\\interpreter\\" + className + ".java";
        String dataPath = System.getProperty("user.dir") + "\\test_pascal\\test_if";
        createTest(className, dataPath, outPath);
    }

    public void testGenerateCaseStatement() throws IOException {
        String className = "CaseStatementTest";
        String outPath = System.getProperty("user.dir")
                + "\\app\\src\\test\\java\\com\\duy\\pascal\\interpreter\\" + className + ".java";
        String dataPath = System.getProperty("user.dir") + "\\test_pascal\\test_case_statement";
        createTest(className, dataPath, outPath);
    }

    public void testGenerateBoolean() throws IOException {
        String className = "BooleanTest";
        String outPath = System.getProperty("user.dir")
                + "\\app\\src\\test\\java\\com\\duy\\pascal\\interpreter\\" + className + ".java";
        String dataPath = System.getProperty("user.dir") + "\\test_pascal\\test_boolean";
        createTest(className, dataPath, outPath);
    }

    public void testGenerateConst() throws IOException {
        String className = "ConstTest";
        String outPath = System.getProperty("user.dir")
                + "\\app\\src\\test\\java\\com\\duy\\pascal\\interpreter\\" + className + ".java";
        String dataPath = System.getProperty("user.dir") + "\\test_pascal\\test_const";
        createTest(className, dataPath, outPath);
    }

    public void testGenerateFunction() throws IOException {
        String className = "FunctionTest";
        String outPath = System.getProperty("user.dir")
                + "\\app\\src\\test\\java\\com\\duy\\pascal\\interpreter\\" + className + ".java";
        String dataPath = System.getProperty("user.dir") + "\\test_pascal\\test_function";
        createTest(className, dataPath, outPath);
    }

    public void testGenerateScope() throws IOException {
        String className = "ScopeTest";
        String outPath = System.getProperty("user.dir")
                + "\\app\\src\\test\\java\\com\\duy\\pascal\\interpreter\\" + className + ".java";
        String dataPath = System.getProperty("user.dir") + "\\test_pascal\\test_scope";
        createTest(className, dataPath, outPath);
    }

    private void createTest(String className, String dataPath, String outPath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/*\n" +
                " *  Copyright (c) 2017 Tran Le Duy\n" +
                " *\n" +
                " * Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                " * you may not use this file except in compliance with the License.\n" +
                " * You may obtain a copy of the License at\n" +
                " *\n" +
                " *     http://www.apache.org/licenses/LICENSE-2.0\n" +
                " *\n" +
                " * Unless required by applicable law or agreed to in writing, software\n" +
                " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                " * See the License for the specific language governing permissions and\n" +
                " * limitations under the License.\n" +
                " */\n" +
                "\n" +
                "package com.duy.pascal.interpreter;\n" +
                "\n" +
                "/**\n" +
                " * Created by Duy on 28-Aug-17.\n" +
                " */\n" +
                "\n" +
                "public class " + className + " extends BaseTestCase {\n" +
                "    @Override\n" +
                "    public String getDirTest() {\n" +
                "        return \"" + dataPath + "\";\n" +
                "    }");
        File file = new File(dataPath);
        for (File f : file.listFiles()) {
            if (f.getName().endsWith(".pas")) {
                stringBuilder.append("public void test").append(f.getName().substring(0, f.getName().lastIndexOf(".")));
                stringBuilder.append("()").append("{").append("run(")
                        .append("\"")
                        .append(f.getName())
                        .append("\"")
                        .append(");").append("}");
            }
        }
        stringBuilder.append("}");
        File out = new File(outPath);
        FileOutputStream fileOutputStream = new FileOutputStream(out);
        fileOutputStream.write(stringBuilder.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

}

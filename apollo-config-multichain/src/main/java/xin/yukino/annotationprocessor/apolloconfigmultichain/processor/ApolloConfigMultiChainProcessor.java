package xin.yukino.annotationprocessor.apolloconfigmultichain.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import java.io.*;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("xin.yukino.annotationprocessor.apolloconfigmultichain.annotation.MultiChainApolloConfigAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ApolloConfigMultiChainProcessor extends AbstractProcessor {

    private Elements elementUtils;

    private Types typeUtils;

    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            // 处理每个使用了 @MultiChainApolloConfigAnnotation 的元素
            annotatedElements.forEach(this::processElement);
        }
        return true;
    }

    private void processElement(Element element) {
        if (element.getKind() != ElementKind.FIELD) {
            return;
        }
        //获取全限类名
        Element classElement = element.getEnclosingElement();
        String fullClassName = classElement.toString();
        String fullClassPath = fullClassName.replace('.', '\\');

        //读取源文件
        File file = new File(fullClassPath + ".java");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            //原代码
            StringBuilder originCode = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                originCode.append(line).append("\n");
            }

            //自定义一个print方法
            String printFuncOne = "\tpublic void print() {\n" +
                    "        System.out.println(\" I am the method created by cusProcessor !!! \");\n" +
                    "        System.out.printf(\"age: %d   name: %s\", age, name);\n" +
                    "    }\n" +
                    "}";

            //在原代码最后 添加方法
            String newCode = originCode.toString().replaceAll("}", printFuncOne);

            //创建一个和原来同名的类
            JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(fullClassName);
            Writer w = fileObject.openWriter();
            w.write(newCode);
            w.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}

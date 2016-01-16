package com.company;

import com.company.ast.objects.ASTClass;
import com.company.ast.objects.ASTMethod;
import com.company.ast.objects.ASTVariable;
import com.company.ast.ASTGenerator;

public class Main {

    public static void main(String[] args) {


        ASTGenerator generator = new ASTGenerator();
        generator.initialize();
        generator.parseFile("./src/com/company/test/B.java");

        ASTClass root = generator.getClass("B.java");

        for(ASTVariable att : root.getAttributes()) {
            System.out.println(att.toString());
        }

        for(ASTMethod method : root.getMethods()) {
            System.out.println(method.toString());
        }
    }
}

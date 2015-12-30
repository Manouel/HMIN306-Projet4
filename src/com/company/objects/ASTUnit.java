package com.company.objects;

import com.company.MethodInvocationVisitor;
import com.company.test.A;
import org.eclipse.jdt.core.dom.*;

import java.util.List;

public class ASTUnit {
	
	private CompilationUnit compilationUnit;
	private TypeDeclaration typeDeclaration;


	private ASTClass unitClass;
	
	public ASTUnit(CompilationUnit cu) {
		compilationUnit = cu;
		typeDeclaration = ((TypeDeclaration) compilationUnit.types().get(0));
	}
	
	private void registerAttributes() {
		FieldDeclaration fields[] = typeDeclaration.getFields();
		for (FieldDeclaration field : fields) {
			String type = field.getType().toString();
			String name = ((VariableDeclarationFragment) field.fragments().get(0)).getName().toString();
			ASTClass klass = new ASTClass(type);
			ASTVariable att = new ASTVariable(name, klass);
			unitClass.addAttribute(att);
		}
	}
	
	private void registerMethods() {
		MethodDeclaration methods[] = typeDeclaration.getMethods();

		for (MethodDeclaration method : methods) {

			ASTMethod md = new ASTMethod(method.getName().toString(), unitClass);

			Type typeReturn = method.getReturnType2();

			if(typeReturn != null) {

				md.setReturnType(new ASTClass(typeReturn.toString()));

				for (Object param : method.parameters()) {
					VariableDeclaration variableDeclaration = (VariableDeclaration) param;
					String type = variableDeclaration.getStructuralProperty(SingleVariableDeclaration.TYPE_PROPERTY).toString();
					ASTVariable var = new ASTVariable(variableDeclaration.getName().toString(), new ASTClass(type));
					md.addParameter(var);
				}

				Block block = method.getBody();
				MethodInvocationVisitor miv = new MethodInvocationVisitor();
				block.accept(miv);

				registerLocalVariables(md, miv);

				unitClass.addMethod(md);

			}

		}


	}
	
	private void registerLocalVariables(ASTMethod md, MethodInvocationVisitor miv) {

		List<VariableDeclarationStatement> variableDeclarations = miv.getVariableDeclarations();
		for (VariableDeclarationStatement var : variableDeclarations) {
			System.out.println("Variable body: " + var.toString());
			System.out.println("Variable type : " + var.getType());
			String name = ((VariableDeclarationFragment) var.fragments().get(0)).getName().toString();
			String type = var.getType().toString();
			ASTVariable local = new ASTVariable(name, new ASTClass(type));
			md.addLocalVariable(local);
		}

	}
	
	private void registerCalledMethods() {
		
	}
	
	public void initializeClass() {
		unitClass = new ASTClass(typeDeclaration.getName().toString());
		registerAttributes();
		registerMethods();
	}

	public ASTClass getUnitClass() {
		return unitClass;
	}
}
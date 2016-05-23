package org.bimserver.serviceplatform.actionmgmt;

import java.io.IOException;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;

public class ActionFactoryBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(ActionFactoryBuilder.class);
	private ActionFactory actionFactory;

	@SuppressWarnings("unchecked")
	public ActionFactoryBuilder(String[] packageNames) {
		try {
			ClassPool pool = ClassPool.getDefault();
			
			ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
			configurationBuilder.addClassLoader(getClass().getClassLoader());
			configurationBuilder.forPackages(packageNames);
			configurationBuilder.setScanners(new SubTypesScanner());
			Reflections reflections = new Reflections(configurationBuilder);
			
			Set<Class<? extends Action>> allClasses = reflections.getSubTypesOf(Action.class);
			LOGGER.info(allClasses.size() + " classes found");
			
			pool.insertClassPath(new LoaderClassPath(getClass().getClassLoader()));

			CtClass actionCreater = pool.makeClass("com.bimserver.serviceplatform.ActionCreaterImpl", pool.get(ActionFactory.class.getName()));

			addConstructor(allClasses, pool, actionCreater);
			addCreateMethod(allClasses, pool, actionCreater);

			actionCreater.setModifiers(actionCreater.getModifiers() & ~Modifier.ABSTRACT);
			
			Class<ActionFactory> c = (Class<ActionFactory>) pool.toClass(actionCreater, getClass().getClassLoader(), getClass().getProtectionDomain());
			actionFactory = c.newInstance();
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	private void addConstructor(Set<Class<? extends Action>> allClasses, ClassPool pool, CtClass actionCreater) throws NotFoundException, CannotCompileException, IOException {
		CtConstructor constructor = new CtConstructor(new CtClass[] {}, actionCreater);
		StringBuilder sb = new StringBuilder();
		actionCreater.addConstructor(constructor);
		sb.append("{");
		for (Class<? extends Action> cl : allClasses) {
			sb.append("add(" + cl.getName() + ".class);");
		}
		sb.append("}");
		constructor.setBody(sb.toString());
	}
	
	private void addCreateMethod(Set<Class<? extends Action>> allClasses, ClassPool pool, CtClass actionCreater) throws NotFoundException, CannotCompileException, IOException {
		CtMethod createMethod = new CtMethod(pool.get(Action.class.getName()), "createInternally", new CtClass[] { pool.get(Class.class.getName()) }, actionCreater);
		StringBuilder sb = new StringBuilder();
		actionCreater.addMethod(createMethod);
		sb.append("{");
		for (Class<? extends Action> clazz : allClasses) {
			pool.get(clazz.getName());
			
			sb.append("  if ($1.getName().equals(\"" + clazz.getName() + "\")) {\n");
			sb.append("    return new " + clazz.getName() + "();\n");
			sb.append("  }\n");
		}
		sb.append("return null;}");
		createMethod.setBody(sb.toString());
	}

	public ActionFactory getActionFactory() {
		return actionFactory;
	}
}
package com.wusd.util;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassUtils {
    public static List<Class<?>> getClazzesByPackage(String packageName) {
        List<Class<?>> clazzes = new ArrayList<>();
        String packageDirName = packageName.replace('.', '/');
        boolean recursive = true;
        Enumeration<URL> urlEnumeration;
        try {
            urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                //协议
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String packagePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, packagePath, recursive, clazzes);
                }
                /*else if ("jar".equals(protocol)) {
                    //如果是jar包文件
                    //定义一个JarFile
                    JarFile jar;
                    try {
                        //获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        //从此jar包中 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        //同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件按
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            //如果是以/开头的
                            if (name.charAt(0) == '/') {
                                //获取后面的字符串
                                name = name.substring(1);
                            }
                            //如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                //如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    //获取包名 包"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                //如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    //如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        //去掉后面的.class 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            clazzes.add(Class.forName(packageName + "." + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {

                    }
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clazzes;
    }

    //以文件的形式获取包路径下的所有class
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
                                                        List<Class<?>> classes) {
        //获取这个目录
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //获取目录下所有文件和文件夹
        File[] dirFiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || file.getName().endsWith(".class");
            }
        });
        for (File dirFile : dirFiles) {
            String dirFileName = dirFile.getName();
            if (dirFile.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + dirFileName,
                        packagePath + "/" + dirFileName, recursive, classes);
            } else {
                String className = packageName + "." + dirFileName.substring(0, dirFileName.length() - 6);
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String firstLetterToLowerCase(String name) {
        if (StringUtils.isNotEmpty(name)) {
            name = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
        }
        return name;
    }
}



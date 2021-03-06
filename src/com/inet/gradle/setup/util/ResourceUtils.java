/*
 * Copyright 2015 i-net software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inet.gradle.setup.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Some Utils for working with resources.
 */
public class ResourceUtils {

    /**
     * Extract a resource file and save it as file.
     * 
     * @param clazz the class name
     * @param name the relative resource file
     * @param dir the directory
     * @return the new file
     * @throws IOException if any error occur
     */
    public static File extract( Class<?> clazz, String name, File dir ) throws IOException {
        File file = new File( dir, name );
        file.getParentFile().mkdirs();
        try (InputStream input = clazz.getResourceAsStream( name )) {
            Files.copy( input, file.toPath(), StandardCopyOption.REPLACE_EXISTING );
        }
        return file;
    }

    /**
     * Unzip it
     * 
     * @param file input zip file
     * @param folder zip file output folder
     */
    public static void unZipIt( File file, File folder ) {
        unZipIt( file, folder, null );
    }

    public static void unZipIt( File file, File folder, Function<String, String> nameClosure ) {
        unZipIt( file, folder, nameClosure, null );
    }

    public static void unZipIt( File file, File folder, Function<String, String> nameClosure, Function<InputStream, InputStream> streamClosure ) {

        // create output directory is not exists
        if( !folder.exists() ) {
            folder.mkdir();
        }

        // get the zip file content
        try (ZipFile zipFile = new ZipFile( file )) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while( entries.hasMoreElements() ) {
                ZipEntry zipEntry = (ZipEntry)entries.nextElement();

                String fileName = zipEntry.getName();
                if( !zipEntry.isDirectory() ) {

                    if( nameClosure != null ) {
                        fileName = nameClosure.apply( fileName );
                    }

                    try (InputStream input = zipFile.getInputStream( zipEntry )) {
                        File target = new File( folder, fileName );
                        target.getParentFile().mkdirs();

                        @SuppressWarnings( "resource" )
                        InputStream stream = input;
                        if( streamClosure != null ) {
                            stream = streamClosure.apply( input );
                        }

                        Files.copy( stream, target.toPath(), StandardCopyOption.REPLACE_EXISTING );
                    }
                }
            }
        } catch( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes directory denoted by given path with its content.
     * 
     * @param directoryPath directory path.
     * @throws IOException if an I/O error occurs, like something cannot be deleted.
     */
    public static void deleteDirectory( Path directoryPath ) throws IOException {
        Files.walkFileTree( directoryPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
                Files.delete( file );
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory( Path dir, IOException exception ) throws IOException {
                if( exception == null ) {
                    Files.delete( dir );
                    return FileVisitResult.CONTINUE;
                } else {
                    throw exception;
                }
            }
        } );
    }
}

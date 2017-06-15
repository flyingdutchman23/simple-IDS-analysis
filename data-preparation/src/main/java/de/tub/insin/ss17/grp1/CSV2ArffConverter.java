
package de.tub.insin.ss17.grp1;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.List;

public class CSV2ArffConverter {
    
    private static final void removeCsvHeader(List<File> csvs) throws IOException{
        boolean flag = false;
        for (File csv: csvs ){
            if(flag){  
                    RandomAccessFile raf = new RandomAccessFile(csv, "rw");                                                    
                    long writePosition = raf.getFilePointer();                            
                    raf.readLine();                                                                                           
                    long readPosition = raf.getFilePointer();                             

                    byte[] buff = new byte[1024];                                         
                    int n;                                                                
                    while (-1 != (n = raf.read(buff))) {                                  
                        raf.seek(writePosition);                                          
                        raf.write(buff, 0, n);                                            
                        readPosition += n;                                                
                        writePosition += n;                                               
                        raf.seek(readPosition);                                           
                    }                                                                     
                    raf.setLength(writePosition);                                         
                    raf.close();                                                          
            }
           flag = true; 
        }
    }

    private static final void transfer(final Reader source, final Writer destination) throws IOException {
        char[] buffer = new char[1024 * 16];
        int len = 0;
        while ((len = source.read(buffer)) >= 0) {
            destination.write(buffer, 0, len);
        }
    }

    private static void appendCSVs(final List<File> csvs, final File combination)
                 throws IOException {

        for (File csv : csvs) {
             try (Reader source = new LineNumberReader(new FileReader(csv));
            Writer destination = new BufferedWriter(new FileWriter(combination, true)); ) {
                
            transfer(source, destination);
             }
        }
    }

    private static File combine(List<File> csvs) throws IOException{
        removeCsvHeader(csvs);
        File combination = File.createTempFile("combination", ".netflow");
        Files.copy(csvs.remove(0).toPath(), combination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        appendCSVs(csvs, combination);
        System.out.println("\n");
        try (BufferedReader br = new BufferedReader(new FileReader(combination))) {
     	   String line = null;
     	   while ((line = br.readLine()) != null) {
     	       System.out.println(line);
     	   }
     	}

        return combination;
    }

    private static File convert(File mergedSrcFile) throws IOException {
        // TODO code is copied from weka website
        CSVLoader loader = new CSVLoader();
        loader.setSource(mergedSrcFile);
        Instances data = loader.getDataSet();

        File arffTmp = Util.saveAsArff(data);

        return arffTmp;
    }

    public static File parse(List<File> csvs) throws IOException {
        File combinedCsv = combine(csvs);
        System.out.println("postCombine:"+csvs+"\n");
//        try (BufferedReader br = new BufferedReader(new FileReader(combinedCsv))) {
//     	   String line = null;
//     	   while ((line = br.readLine()) != null) {
//     	       System.out.println(line);
//     	   }
//     	}
        File combinedArff = convert(combinedCsv);
        return combinedArff;
    }
}

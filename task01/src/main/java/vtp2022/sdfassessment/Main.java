package vtp2022.sdfassessment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String csvPath;
        String templatePath;
        HashMap<String, Integer> varNames = new HashMap<>();
        List<String[]> data = new ArrayList<>();
        
        csvPath = args[0];
        templatePath = args[1];
        
        // read CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            // first row = variable names, no spaces
            if ((line = br.readLine()) != null) {
                String[] vars = line.split(",");
                for (int i = 0; i < vars.length; i++) {
                    varNames.put(vars[i],i);
                }
            }
            // subsequent rows are the data
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        
        String line;
        String var;
        String word;

        for (String[] row : data) {
            // substitute into the template file
            try (BufferedReader br = new BufferedReader(new FileReader(templatePath))) {
                while ((line = br.readLine()) != null) {
                    String[] lineArr = line.split(" ");
                    for (int i = 0; i < lineArr.length; i++) {
                        word = lineArr[i];
                        StringBuilder prefix = new StringBuilder();
                        StringBuilder suffix = new StringBuilder();
                        if (word.matches("(.*)?__.*__(.*)?")) {
                            // get variable name
                            StringBuilder varSb = new StringBuilder();
                            int count = 0;
                            // remove __ twice
                            for (int j = 0; j < word.length(); j++) {
                                if (count < 2 && word.charAt(j) == '_' &&
                                    word.charAt(j+1) == '_') {
                                        j += 1;
                                        count += 1;
                                        continue;
                                    }
                                if (count == 1) {
                                    // we have encountered the first __
                                    // so any character until we encounter the second __
                                    // is part of the variable name
                                    varSb.append(word.charAt(j));
                                } else if (count == 0) {
                                    prefix.append(word.charAt(j));
                                } else {
                                    suffix.append(word.charAt(j));
                                }
                            }
                            var = varSb.toString();
                            // get position of variable
                            int idx = varNames.get(var);
                            // replace variable with data
                            prefix.append(row[idx]);
                            prefix.append(suffix.toString());
                            lineArr[i] = prefix.toString();
                        }
                    }
                    // print out line
                    System.out.println(String.join(" ",lineArr));
                }
                System.out.println("-----------");
            } catch (Exception e){
                e.printStackTrace();
            }
            
        }

    }
}
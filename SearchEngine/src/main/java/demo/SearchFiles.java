/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package demo;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.Tika;

/** Simple command-line based search demo. */
public class SearchFiles {
    
    private static String queryString = null;
    private static List resultlist = new ArrayList();
    private static HttpServletRequest request=null;

  private SearchFiles() {}

  /** Simple command-line based search demo. */
  public static List search(String myquerystring, HttpServletRequest myrequest ) throws Exception {

    request = myrequest;  
    resultlist = new ArrayList();
    String index = "index";
    String field = "contents";
    String queries = null;
    int repeat = 0;
    boolean raw = false;
    
    int hitsPerPage = 10;
    
    /*
    for(int i = 0;i < args.length;i++) {
      if ("-index".equals(args[i])) {
        index = args[i+1];
        i++;
      } else if ("-field".equals(args[i])) {
        field = args[i+1];
        i++;
      } else if ("-queries".equals(args[i])) {
        queries = args[i+1];
        i++;
      } else if ("-query".equals(args[i])) {
        queryString = args[i+1];
        i++;
      } else if ("-repeat".equals(args[i])) {
        repeat = Integer.parseInt(args[i+1]);
        i++;
      } else if ("-raw".equals(args[i])) {
        raw = true;
      } else if ("-paging".equals(args[i])) {
        hitsPerPage = Integer.parseInt(args[i+1]);
        if (hitsPerPage <= 0) {
          System.err.println("There must be at least 1 hit per page.");
          System.exit(1);
        }
        i++;
      }
    }
    */
    
    //index =  "D:/test/lucene/index";
    
    Configurations configs = new Configurations();
try
{
    Configuration config = configs.properties(new File("config.properties"));
    // access configuration properties
    index = config.getString("index.path");
    System.out.println("index.path="+index);

}
catch (ConfigurationException cex)
{
    // Something went wrong
    cex.printStackTrace();
}
    
    
    
     queryString = myquerystring;
     
    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
    IndexSearcher searcher = new IndexSearcher(reader);
    Analyzer analyzer = new StandardAnalyzer();

    BufferedReader in = null;
    if (queries != null) {
      in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
    } else {
      in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    }
    QueryParser parser = new QueryParser(field, analyzer);
    while (true) {
      if (queries == null && queryString == null) {                        // prompt the user
        System.out.println("Enter query: ");
      }

      String line = queryString != null ? queryString : in.readLine();

      if (line == null || line.length() == -1) {
        break;
      }

      line = line.trim();
      if (line.length() == 0) {
        break;
      }
      
      Query query = parser.parse(line);
      System.out.println("Searching for: " + query.toString(field));
            
      if (repeat > 0) {                           // repeat & time as benchmark
        Date start = new Date();
        for (int i = 0; i < repeat; i++) {
          searcher.search(query, 100);
        }
        Date end = new Date();
        System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
      }

      doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);

      if (queryString != null) {
        break;
      }
    }
    reader.close();
    
    return resultlist;
  }

  /**
   * This demonstrates a typical paging search scenario, where the search engine presents 
   * pages of size n to the user. The user can then go to the next page if interested in
   * the next hits.
   * 
   * When the query is executed for the first time, then only enough results are collected
   * to fill 5 result pages. If the user wants to page beyond this limit, then the query
   * is executed another time and all hits are collected.
   * 
   */
  public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, 
                                     int hitsPerPage, boolean raw, boolean interactive) throws IOException {
 
    // Collect enough docs to show 5 pages
    TopDocs results = searcher.search(query, 5 * hitsPerPage);
    ScoreDoc[] hits = results.scoreDocs;
    
    //int numTotalHits = Math.toIntExact(results.totalHits);
    
    Long xxx  = new Long(results.totalHits);
    int numTotalHits = xxx.intValue();
    
    System.out.println(numTotalHits + " total matching documents");

    int start = 0;
    int end = Math.min(numTotalHits, hitsPerPage);
        
    while (true) {
      if (end > hits.length) {
        System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
        System.out.println("Collect more (y/n) ?");
        String line = in.readLine();
        if (line.length() == 0 || line.charAt(0) == 'n') {
          break;
        }

        hits = searcher.search(query, numTotalHits).scoreDocs;
      }
      
      end = Math.min(hits.length, start + hitsPerPage);
      
      for (int i = start; i < end; i++) {
        if (raw) {                              // output raw format
          System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
          continue;
        }

        Document doc = searcher.doc(hits[i].doc);
        String path = doc.get("path");
        if (path != null) {
            
          System.out.println((i+1) + ". " + path);
          
          String myfilepath = (i+1) + ". " + path;
          
          //resultlist.add(myfilepath);
          //String filename= path.substring(path.lastIndexOf("/")+1,path.length()); // for Linux
          String filename= path.substring(path.lastIndexOf("\\")+1,path.length()); // for Windows

          
          String contextpath = request.getContextPath();
          System.out.println("contextpath="+contextpath);
          
          resultlist.add("<a href='"+contextpath+"/download/"+filename+"'>"+myfilepath+"</a>");
          
          copyfile(path);
          
          String title = doc.get("title");
          if (title != null) {
            System.out.println("   Title: " + doc.get("title"));
          }
          
          String data = opentext(path);
          show_occur(queryString,data);
          
        } else {
          System.out.println((i+1) + ". " + "No path for this document");
        }
                  
      }

      if (!interactive || end == 0) {
        break;
      }

      if (numTotalHits >= end) {
        boolean quit = false;
        while (true) {
          System.out.print("Press ");
          if (start - hitsPerPage >= 0) {
            System.out.print("(p)revious page, ");  
          }
          if (start + hitsPerPage < numTotalHits) {
            System.out.print("(n)ext page, ");
          }
          System.out.println("(q)uit or enter number to jump to a page.");
          
          String line = in.readLine();
          if (line.length() == 0 || line.charAt(0)=='q') {
            quit = true;
            break;
          }
          if (line.charAt(0) == 'p') {
            start = Math.max(0, start - hitsPerPage);
            break;
          } else if (line.charAt(0) == 'n') {
            if (start + hitsPerPage < numTotalHits) {
              start+=hitsPerPage;
            }
            break;
          } else {
            int page = Integer.parseInt(line);
            if ((page - 1) * hitsPerPage < numTotalHits) {
              start = (page - 1) * hitsPerPage;
              break;
            } else {
              System.out.println("No such page");
            }
          }
        }
        if (quit) break;
        end = Math.min(numTotalHits, start + hitsPerPage);
      }
    }
  }
  
    public static String opentext(String filepath) {
        // TODO code application logic here
        
        //Locale.setDefault(new Locale("th","TH"));
        Tika tika = new Tika();
        String result="";
        
        try
        {
            
            
            File file = new File(filepath);
            result = tika.parseToString(file);
            //System.out.println(result);
            
            return result;
                    
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return result;
    }
  
    public static void show_occur(String keyword,String data) {
        
        String[] datan = data.split("\n");
        String upperkeyword= keyword.toUpperCase();
        
        for (int i=0;i< datan.length; i++)
        {
        	String lineno = String.valueOf(i+1);
            String upperdata  = datan[i].toUpperCase();
            int pos = upperdata.indexOf(upperkeyword);
            if (pos >=0) {
                System.out.println("line "+lineno+": "+datan[i]);
                resultlist.add("line "+lineno+": "+datan[i]);
            }
        }
        
        
    }
    
    public static void copyfile(String sourcefilepath)
    {
        //String filename= sourcefilepath.substring(sourcefilepath.lastIndexOf("/")+1,sourcefilepath.length()); // for Linux
        String filename= sourcefilepath.substring(sourcefilepath.lastIndexOf("\\")+1,sourcefilepath.length()); // for windows
        
        System.out.println("copy filename="+filename);
        System.out.println("copy src filepath="+sourcefilepath);
        
      //String destfilepath = request.getRealPath("/download")+"/"+filename; //for Linux
        String destfilepath = request.getRealPath("/download")+"\\"+filename; // for Windows

        System.out.println("copy dest filepath="+destfilepath);
        
        
        try {
            FileUtils.copyFile(new File(sourcefilepath), new File(destfilepath));
        } catch (IOException ex) {
            Logger.getLogger(SearchFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}

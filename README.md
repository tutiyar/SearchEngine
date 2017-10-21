Smart Search is the web-based search engine build on top from Apache Lucene and Apache Tika library.

user manual:
After user input keyword for Search,  the result will show line match for all occurrences. 
And user can download files from links.

This program was written by mr. Tutiya Rayanak. 
License: Free for All used. (Apache License 2.0)  
Contact Email: tutiya.rayanak@gmail.com

It is MAVEN project.
It is easy for use, to deploy and config. 

--------------------------------------------------------
Programming Reference:

1 Config.properties
sample for windows:
index.path=D:/test/lucene/index
document.path=D:/test/lucene/docs

sample for Linux:
index.path=/home/tutiya/test/lucene/index
document.path=/home/tutiya/test/lucene/docs

----------------------------------------------------------
2. Indexfiles.java
Line: 124-125
//Analyzer analyzer = new StandardAnalyzer();
Analyzer analyzer = new ThaiAnalyzer();

Use Standard Analyzer for  English.
Use Thai Analyzer for Thai.

----------------------------------------------------------
3. SearchFiles.java

Line: 231-232
//String filename= path.substring(path.lastIndexOf("/")+1,path.length()); // for Linux
String filename= path.substring(path.lastIndexOf("\\")+1,path.length()); // for Windows

Line:348-349
//String filename= sourcefilepath.substring(sourcefilepath.lastIndexOf("/")+1,sourcefilepath.length()); // for Linux
String filename= sourcefilepath.substring(sourcefilepath.lastIndexOf("\\")+1,sourcefilepath.length()); // for windows


Line:354-355
//String destfilepath = request.getRealPath("/download")+"/"+filename; //for Linux
String destfilepath = request.getRealPath("/download")+"\\"+filename; // for Windows








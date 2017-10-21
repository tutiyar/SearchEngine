Smart Search is the web-based search engine build on top from Apache Lucene and Apache Tika library.<br/>
<br/>
user manual:<br/>
After user input keyword for Search,  the result will show line match for all occurrences.<br/> 
And user can download files from links.<br/>
<br/>
This program was written by mr. Tutiya Rayanak.<br/> 
Contact Email: tutiya.rayanak@gmail.com<br/>
License: Free for All used. (Apache License 2.0)<br/>  
<br/>
It is MAVEN project.<br/>
It is easy for use, to deploy and config.<br/> 
<br/>
--------------------------------------------------------
Programming Reference:<br/>
<br/>
1 Config.properties<br/>
sample for windows:<br/>
index.path=D:/test/lucene/index<br/>
document.path=D:/test/lucene/docs<br/>
<br/>
sample for Linux:<br/>
index.path=/home/tutiya/test/lucene/index<br/><br/>
document.path=/home/tutiya/test/lucene/docs<br/><br/>

----------------------------------------------------------
2. Indexfiles.java<br/>
Line: 124-125<br/>
//Analyzer analyzer = new StandardAnalyzer();<br/>
Analyzer analyzer = new ThaiAnalyzer();<br/>
<br/>
Use Standard Analyzer for  English.<br/>
Use Thai Analyzer for Thai.<br/>
<br/>
----------------------------------------------------------
3. SearchFiles.java<br/>
<br/>
Line: 231-232<br/>
//String filename= path.substring(path.lastIndexOf("/")+1,path.length()); // for Linux<br/>
String filename= path.substring(path.lastIndexOf("\\")+1,path.length()); // for Windows<br/>
<br/>
Line:348-349<br/>
//String filename= sourcefilepath.substring(sourcefilepath.lastIndexOf("/")+1,sourcefilepath.length()); // for Linux<br/>
String filename= sourcefilepath.substring(sourcefilepath.lastIndexOf("\\")+1,sourcefilepath.length()); // for windows<br/>


Line:354-355
//String destfilepath = request.getRealPath("/download")+"/"+filename; //for Linux
String destfilepath = request.getRealPath("/download")+"\\"+filename; // for Windows








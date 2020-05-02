# Eιρήνη שָׁלוֹם

## The verses references list :
The user needs to give a list of biblical verses references to class the associated verses.<br />
This list needs to be located on ```/Model/Data/```.<br /><br />
It has to be a .csv file named ```List_of_verses``` (so the final name should be ```List_of_verses.csv```).<br />
Each references given in the list has to be separated by a comma and an end of line.<br />
The first line of this list is dedicated to inform the program which verse division the program has to use with these references.<br />
So, the very first line of the file has to follow that syntax : ```verses_division_:_X,```, where X is the name of the verse division.<br /><br />
The program understand those names as verse division :<br />
- for the Stephanus 1511 verse division : "KJV", "KJTR", "CNTR", "Martin".<br />
- for the NA28 : "NA28", "Darby".<br />

You have an example of a such list in this GitHub repo.<br />

## Here are some commands : 

To start the programm with the list of verses (located on ```/Model/Data/List_of_verses.csv```) : 
```
cd Diverse && javac @java_files.txt -d Generated\ Java\ classes/ && cd Generated\ Java\ classes/ && java Start && cd ../..
```

To compile all .java files and execute a test, still from this folder :
```
cd Diverse && javac @java_files.txt -d Generated\ Java\ classes/ && cd Generated\ Java\ classes/ && java TestToGetGreekText && cd ../..
````

To compile, from this folder, the source code :
```
javac Diverse/@java_files.txt -d Diverse/Generated\ Java\ classes/
```
<br />
The Greek texts, the morphology and strongs numbers are from the CNTR (https://greekcntr.org/).

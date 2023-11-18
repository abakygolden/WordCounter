# WordCounter

I have taken the approach to start by analysing the problem and making respective diagrams of how I think it should be implemented. 

Of course, I lack access to the stake-holders of this challenge and this being a "quick delivery" project some points has been taken into consideration that were never confirmed by the challenge ( for example maximum number of lines per file)  that should allow the program to run smoothly and efficiently . If needed be this can be change in the future to enhance the program.

The functionality of the program is divided in two main areas, a file-reading and a file-writing. 

In each of them the logic of grouping the words happens. 

Below you can see the drawings of the analysis for this solution 
## Main Flow
![img.png](docs/main_flow.png)
## Fail Reader Functionality 
![img.png](docs/file_reader.png)
## File Writer Functionality 
![img.png](docs/file_writer.png)

----
### Decisions taken 
- Any word not being starting with the 27 letters of english alphabet will be ignored
- Any exception that causes the program to stop will clear the output files, as corrupted data might be worse than no data.
- Words consisting of special characters as "back-end" would be separated into their sub-parts ("back" and "end")
- Acronyms if written with special characters would be considered different words, if no special character then it would be considered one word,  for example : (C.I.A --> "C", "I", "A" ; FBI --> "FBI")

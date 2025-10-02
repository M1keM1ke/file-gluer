# File Gluer

### Allow you to glue files in selected directory and write them in result file
#### The most useful use case - add content of the result file in LLM

## Options
| Option              | Required | Description                                           |
|---------------------|----------|-------------------------------------------------------|
| -i, --input <arg>   | true     | input directory for gluing files                      |
| -o, --output <arg>  | true     | output file path                                      |
| -ig, --ignore <arg> | false    | ignore file like .gitignore                           |
| -s, --separator     | false    | file separator. Default separator is: --------------- |
| -h, --help          | false    | show help                                             |

## Usage
1) Run file-gluer.jar located at ./build/libs like: java -jar file-gluer.jar -i dir-to-gluing-files -o output-file-path
2) Add env to PATH - dir to file filegluer.bat, then run from anywhere: filegluer -i dir-to-gluing-files -o output-file-path

## Example of ignore file
You can add ignore file containing dirs and files witch you are want 
to be excluded (option -ig). Syntax are similar to .gitignore.
For example, glue only source files:
```
*
!/src/
!/src/**
```
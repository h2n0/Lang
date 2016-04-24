# Unnamed language (Algalang?)
## Language outline
1. Strict typed language.
2. Reads down the file looking for functions that separates them into smaller "programs"
3. Reads down again looking for global variables.
4. Variables are defined like so. `a:int = 2` or they can be dynamically assigned `b := "hello"`
5. The program should start from a entry point like Java's main function. probably going to go for an "onStart" function
6. Basic output will be enough for now. e.g. `out("Hello");` or `out(3 + 4);`

## Function declaration

1. There will be two kinds of functions, inline and block.
2. Inline functions will look like so. `function d:int () -> return 3 + 4;`
3. Block functions will work like so.  `function d:string () { return "Hello World!"; }`

## Arithmetic
1. Basic arithmetic should be in the initial version, with more complex things to arrive later on.
2. The following are the minimum we want to launch with. `+`,`-`,`/`,`*`,`!`.
3. Later on I think that adding various others would help greatly for example.
4. `PI`,`sin`,`cos`,`tan`,`abs`.

## Example program
```
num x = 3;
num y = 4;
string name = "Dave";

x += 7;
y -= 1;

num z = x + y;

out z;
out name;
```

When this example is run, we should get the output as '13 "Dave"'.

## Completion list

Completed:
  - Basic parsing of file.
  - Variables can be manipulated.
  - Functions can be created and called.
  - Can add special variables such as "pi" with ease.

Todo (in order of importance):
  - Local variables in functions.
  - 

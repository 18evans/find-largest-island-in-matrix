# Find Largest island in a matrix
An exercise using a variation of the Flood Fill algorithm.
Solution to the exercise given in freeCodingCamp's [Flood Fill Algorithm Explained](https://www.freecodecamp.org/news/flood-fill-algorithm-explained/) article.


## Description:
Copied from the article.

In a Bidimensional array you are given n number of “islands”. Try to find the largest area of an island and the corresponding island number. 0 marks water and any other x between 1 and n marks one square from the surface corresponding to island x.

### Input

* n - the number of islands.
* l,c - the dimensions of the matrix.
* the next l lines, c numbers giving the lth row of the matrix.

### Output

* i - the number of the island with the largest area.
* A - the area of the i‘th island.

Ex:

You have the following input:
```
2 4 4
0 0 0 1
0 0 1 1
0 0 0 2
2 2 2 2
```
For which you will get island no. 2 as the biggest island with the area of 5 squares.
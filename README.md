# Pattern Recognition

This API has the objective to manipulate points in a plane(insert, get, delete) and determine every line that contains at least n points.  
All points in the plane are stored in a Set<Point> called pointSet.


## POST /point

- Request: BODY{ "x": ..., "y": ... } which refers to the point coordinates to insert.
- Purpose: add a point in the plane.
- Response: OK if point has been inserted; KO if the point is already present in the plane


## GET /space

- Request: /
- Purpose: get all points in the space.
- Response: OK - Set<Point> if there is at least 1 point in the plane; KO otherwise.


## DELETE /space

- Request: /
- Purpose: remove all points from the plane. 
- Response: OK if there is at least 1 point to remove; KO otherwise


## GET /lines/{n}

- Request: PATH{n} which refers to the minimum amount of points needed to have a valid line
- Purpose: return all lines passing through at least n points.
- Response: List<Line> where each line is composed by a Set<Point>
- Base cases: (p = number of points in the plane; n = request number)
	- p<2 -> 0 lines.
	- n<2 -> 0 lines(actually infinite lines for n=1).
	- n>p -> 0 lines.
- Algorithm: basically the algorithm is made up of two main methods: pairs creation and lines retrieval. Let's analyze both of them.

### Pairs creation (createPairList)

Complexity: n(n-1)/2

Given two points, there is exactly one line passing through those points. This means that given N points in the plane there are maximum N(N-1)/2 lines passing through them.  
For this reason the method createPairList matches each point with every other point in the plane (avoiding duplicates like A-B/B-A). This matches actually represent all the possible line you could find with those points.

Example: Points(A,B,C,D,E)  
Matches:  5(4)/2 = 10

| A-B | B-C | C-D | D-E |  --  |
|-----|-----|-----|-----|-----|
| A-C | B-D | C-E |  --  |  --  |
| A-D | B-E |  --  |  --  |  --  |
| A-E |  --  |  --  |  --  |  --  |
|  --  |  --  |  --  |  --  |  -- |

### Linse retrieval (retrieveLines)

Complexity: n(n-1)(n-2)/2

For every of the N(N-1)/2 pairs, check if each of the N-2 remaining points belongs to the line passing through the considered pair.  
1) Create the line passing through the two points of the pair (if not present already). Each line can be horizontal (pair points have same X), vertical (pair points have same Y) or oblique (else).
2) Insert other points in the line:
- Horizontal: check if the point has same X.
- Vertical: check if the point has same Y.
- Oblique: calculate the line equation (y = mx + q). Check if the point belongs the line. 
3) Check if line is valid (points belonging the line >= n).

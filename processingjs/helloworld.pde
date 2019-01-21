int i = 0;
int w = 801;
int h = 601;

int basesDeployed = 0;
int nodesDeployed = 0;

class Point {
	int x;
	int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

int[][] positions = new int[2][2];
ArrayList nodes = new ArrayList();
void setup() {
	size(w, h, P2D);
	background(255);
	smooth();
	strokeWeight(1);
	frameRate(24);

	rectMode(CENTER);
	ellipseMode(CENTER);
	noCursor();
}
/*
int closer(Point p) {
	int deltax = abs(positions[0][0] - p.x);
	int deltay = abs(positions[0][1] - p.y);
	int dist = (deltax*deltax) + (deltay*deltay)

	deltax = abs(positions[1][0] - p.x);
	deltay = abs(positions[1][1] - p.y);
	int dist2 = (deltax*deltax) + (deltay*deltay)

	if (dist < dist2) {
		return 0;
	} else {
		return 1;
	}

}
*/
void draw() {
	fill(255);
	stroke(66,66,66,100);
	for (int i = 0; i <= w ; i += 10) {
		line(i, 0, i, h);
	}
	for (int i = 0; i <= h ; i += 10) {
		line(0, i, w, i);
	}

	for (int i = 0; i < nodes.size(); i++) {
		fill(200);
		Point p = nodes.get(i);
		int closerBase = 0;//closer(p);
		stroke(0);
		line(positions[closerBase][0], positions[closerBase][1], p.x, p.y);
		noStroke();
		ellipse(round(p.x / 10) * 10, round(p.y / 10) * 10, 38, 38);
	}
	for (int i = 0; i < basesDeployed; i++) {
		fill(128);
		rect(round(positions[i][0] / 10) * 10, round(positions[i][1] / 10) * 10, 58, 58);		
	}

	fill(255);
	noStroke();
	if (basesDeployed < 2) {
		rect(round(mouseX / 10) * 10 + 1, round(mouseY / 10) * 10 + 1, 58, 58);
	} else {
		ellipse(round(mouseX / 10) * 10 + 1, round(mouseY / 10) * 10 + 1, 38, 38);
	}
}

void mouseClicked() {
	if (basesDeployed < 2) {
		positions[basesDeployed][0] = mouseX;
		positions[basesDeployed][1] = mouseY;
		basesDeployed += 1;

	} else {
		nodes.add(new Point(mouseX, mouseY));
	}
}
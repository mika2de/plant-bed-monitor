//color("azure",0.25) cube([8.7,2.9,4.1]);

module sensor() {
    translate([-6,0,0]) cube([8,2.2,0.2,]);
    translate([0.5,0,0]) cylinder(0.2,r=0.2,false,$fn=6);
    translate([0.5,2.2,0]) cylinder(0.2,r=0.2,false,$fn=6);
}



module bottom() {
    difference() {
        translate([-0.4,-0.2,-1.2]) cube([9  ,2.6,1.1]);
        translate([0   ,0   ,-1  ]) cube([8.4,2.2,1.1]);
        translate([0   ,0   ,-0.2]) sensor();
        translate([3.8 ,-0.2,-1.8]) cube([4.6,2.6,2  ]);
    }
    translate([-0.4,-0.2,-1.0]) cube([3,2.6,0.2]);
    
    difference() {
        translate([3.6,-0.4,-2.2]) cube([5,3  ,2.1]);
        translate([3.8,-0.2,-2.1]) cube([4.6,2.6,2.1]);
        translate([0   ,0   ,-1  ]) cube([8.4,2.2,1.1]);
    }
}

module top() {
    difference() {
        translate([-0.4,-0.2,0]) cube([9  ,2.6,1.4]);
        translate([-0.2,0,-0.04]) cube([9,2.2,1.4]);
        translate([-0.3,1.3,0.4]) cylinder(0.5,0.4,0.5,false,$fn=40);
    }
    difference() {
        translate([3.6,-0.4,0]) cube([5,3  ,1.4]);
        translate([3.8,-0.2,-0.04]) cube([4.6,2.6,1.4]);
        translate([0  ,0   ,-0.04]) cube([8.4,2.2,1.4]);
    }
    
}

top();
// bottom();
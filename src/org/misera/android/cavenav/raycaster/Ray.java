package org.misera.android.cavenav.raycaster;

public class Ray
{
	public int id = 0;
	public double distance = 0;
	public int sliceHeight = 0;
	
	public Ray(int id, double distance, int sliceHeight){
		this.id = id;
		this.distance = distance;
		this.sliceHeight = sliceHeight;
	}
}

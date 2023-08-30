package fr.sushi.relica.client.renderer;

public class UvMap {

    public float u0, v0;
    public float u1, v1;
    public float u2, v2;
    public float u3, v3;

    public UvMap(int width, int height, int u0, int v0, int u1, int v1, int u2, int v2, int u3, int v3) {
        this.u0 = (float)u0/width;
        this.v0 = (float)v0/height;
        this.u1 = (float)u1/width;
        this.v1 = (float)v1/height;
        this.u2 = (float)u2/width;
        this.v2 = (float)v2/height;
        this.u3 = (float)u3/width;
        this.v3 = (float)v3/height;
    }
}

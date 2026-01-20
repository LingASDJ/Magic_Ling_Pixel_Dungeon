package com.shatteredpixel.shatteredpixeldungeon.levels;

public class SokobanLayouts {

    //32X32
    private static final int W = Terrain.WALL;
    private static final int T = Terrain.WATER;
    private static final int Z = Terrain.HIGH_GRASS;
    private static final int D = Terrain.DOOR;
    private static final int L = Terrain.DOOR;
    private static final int Q = Terrain.WATER; //for readability

    //private static final int T = Terrain.INACTIVE_TRAP;

    private static final int E = Terrain.EMPTY;
    //private static final int X = Terrain.EXIT;

    private static final int M = Terrain.WALL_DECO;
    //private static final int P = Terrain.PEDESTAL;

    private static final int A = Terrain.EMPTY;
    private static final int X = Terrain.EMPTY;
    private static final int C = Terrain.EMPTY;
    private static final int B = Terrain.EMPTY;
    private static final int H = Terrain.EMPTY;
    private static final int I = Terrain.EMPTY;
    private static final int F = Terrain.EMPTY;
    private static final int U = Terrain.STATUE;
    private static final int G = Terrain.EMPTY;
    private static final int S = Terrain.SECRET_DOOR;
    private static final int R = Terrain.EMPTY;
    private static final int V = Terrain.EMPTY;

    public static final int[] SOKOBAN_INTRO_LEVEL =	{
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	E, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	U, 	A, 	U, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	S, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	U, 	F, 	U, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	C, Q, 	W, Q, 	C, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	U, 	U, Q, 	D, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	G, Q, Q, Q, 	G, Q, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	U, 	A, 	U, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, Q, Q, 	S, Q, Q, Q, Q, 	D, 	F, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	H, 	W, 	W, 	U, 	U, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	U, 	F, 	U, Q, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	F, 	A, Q, 	W, Q, Q, Q, Q, 	W, Q, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	U, 	F, 	U, Q, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	U, 	X, 	U, 	W, Q, Q, Q, Q, 	W, Q, 	C, Q, Q, 	U, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	U, 	F, 	U, Q, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	H, 	U, 	U, 	F, 	D, Q, Q, 	C, Q, 	W, Q, 	G, Q, Q, 	F, 	D, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	G, 	U, 	F, 	U, 	G, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	S, 	W, 	W, 	W, 	W, Q, Q, Q, Q, 	W, Q, Q, Q, Q, 	U, 	W, 	C, 	G, 	G, Q, 	W, 	W, 	W, 	W, 	W, 	C, 	U, 	H, 	U, 	C, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	W, 	W, 	W, 	W, Q, Q, Q, Q, 	D, Q, Q, Q, Q, Q, 	W, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	S, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	U, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	S, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, 	U, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, Q, 	B, Q, Q, Q, 	D, 	F, Q, Q, Q, 	D, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	D, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, 	W, 	U, 	U, 	U, Q, 	W, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, Q, Q, 	R, Q, Q, 	W, 	B, 	B, Q, Q, 	W, Q, 	G, Q, Q, 	W, Q, Q, Q, Q, 	W, Q, 	H, 	W, 	W, 	W, 	W, 	W, Q, Q, 	B, 	W, Q, 	F, Q, 	A, 	I, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, 	W, 	U, 	U, 	U, Q, 	W, Q, 	G, Q, Q, 	W, Q, Q, Q, Q, 	D, Q, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, 	W, 	H, 	F, Q, Q, 	W, Q, 	G, Q, Q, 	W, Q, 	I, 	A, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	D, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	C, Q, Q, 	W, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, 	L, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, Q, Q, 	X, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, Q, Q, 	I, 	V, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W

    };

    public static final int[] SOKOBAN_CASTLE =	{
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,I,T,I,T,I,T,I,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,W,
            W,T,T,W,S,W,W,W,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,W,W,W,W,W,T,T,W,
            W,T,T,W,R,U,Q,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,Q,F,H,W,T,T,W,
            W,T,T,W,U,U,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,F,F,W,T,T,W,
            W,T,T,W,Q,Q,Q,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,Q,Q,Q,W,T,T,W,
            W,T,T,W,W,Q,W,W,W,W,W,W,W,W,W,W,W,W,W,W,M,W,W,H,W,H,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,Q,W,W,T,T,W,
            W,T,T,W,W,Q,W,W,W,W,G,Q,W,F,G,C,F,Q,Q,F,Q,B,W,F,W,F,W,F,F,H,F,H,F,F,F,F,W,Q,Q,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,H,Q,S,F,F,X,F,Q,C,U,Q,Q,W,Q,W,Q,W,F,F,F,F,F,F,F,F,F,S,Q,X,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,Q,Q,W,F,F,X,F,G,C,U,Q,Q,W,Q,W,Q,W,F,F,F,F,F,F,F,F,F,W,Q,Q,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,F,F,W,H,F,Q,F,G,H,U,Q,Q,W,Q,W,Q,W,Q,X,X,X,X,X,Q,Q,Q,W,F,F,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,W,W,W,W,W,W,W,W,L,W,W,W,W,Q,B,Q,M,W,W,W,D,W,W,W,W,S,W,W,S,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,W,W,W,W,W,W,Q,F,F,F,Q,W,W,Q,M,Q,M,W,H,F,U,F,H,W,W,W,W,W,Q,Q,Q,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,W,W,W,W,W,W,U,F,U,F,U,W,W,Q,M,Q,M,W,U,U,Q,U,U,W,W,W,W,W,U,Q,H,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,W,W,W,W,W,W,Q,X,G,X,Q,W,W,Q,M,Q,M,W,Q,G,Q,G,Q,W,W,W,W,W,S,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,W,W,W,W,W,W,Q,C,G,C,Q,W,W,Q,M,Q,M,W,C,G,Q,G,C,W,W,H,H,Q,Q,M,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,W,W,W,W,W,W,W,W,D,W,W,W,W,Q,M,Q,M,W,W,W,S,W,W,W,W,Q,Q,X,Q,M,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,U,U,U,C,L,Q,W,W,W,F,W,W,W,W,Q,M,Q,W,W,W,W,F,W,W,W,W,U,Q,U,U,M,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,H,F,G,Q,W,Q,W,W,W,F,W,W,W,W,Q,W,Q,W,W,W,W,F,W,W,W,W,Q,Q,Q,Q,M,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,U,U,U,B,W,Q,W,W,W,G,G,I,W,W,Q,W,Q,W,W,Q,G,F,W,W,W,W,Q,Q,Q,Q,M,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,W,W,W,W,Q,W,W,W,X,Q,C,W,W,G,W,G,W,W,C,A,G,W,W,W,W,Q,Q,Q,Q,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,Q,U,U,F,D,Q,W,W,W,Q,Q,C,Q,G,X,C,X,G,Q,Q,C,Q,W,W,W,W,Q,Q,Q,Q,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,I,C,Q,F,W,Q,W,W,W,W,W,W,W,W,C,E,C,W,W,W,W,W,W,Q,W,W,W,W,Q,W,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,G,X,Q,Q,W,X,Q,Q,Q,Q,Q,Q,Q,G,X,C,X,G,Q,Q,Q,Q,Q,X,Q,W,W,W,L,W,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,Q,U,U,Q,W,Q,W,W,M,W,W,M,W,W,G,W,G,W,W,W,W,W,W,W,Q,W,Q,Q,Q,Q,Q,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,W,W,W,W,Q,W,F,F,F,F,F,U,W,Q,W,Q,W,W,W,W,W,W,W,Q,W,U,U,Q,U,U,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,U,U,Q,Q,W,Q,W,F,F,F,F,F,U,W,Q,W,Q,W,W,W,W,W,W,W,Q,W,Q,G,X,G,I,S,S,Q,W,T,T,W,W,
            W,Q,T,W,W,Q,W,R,L,F,F,X,Q,W,Q,W,F,F,H,F,F,U,W,Q,W,Q,W,W,W,W,W,Q,Q,Q,W,U,F,Q,F,U,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,H,U,G,G,D,Q,W,F,F,F,F,F,U,W,Q,W,Q,W,W,W,W,W,Q,Q,Q,Q,Q,Q,X,Q,Q,W,W,Q,W,T,T,W,M,
            W,T,T,W,W,Q,W,W,W,F,U,Q,C,W,Q,W,F,F,F,F,F,U,W,Q,W,Q,W,W,W,W,W,Q,Q,W,W,U,Q,X,Q,U,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,S,W,W,W,W,Q,W,Q,X,X,Q,Q,Q,W,Q,W,Q,W,W,W,W,W,Q,W,W,W,Q,Q,Q,Q,Q,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,A,W,W,W,W,D,W,W,W,W,L,W,W,W,Q,W,Q,W,W,H,S,I,Q,Q,W,W,W,W,W,W,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,G,W,W,W,W,Q,Q,Q,Q,Q,Q,Q,G,W,Q,W,Q,W,W,W,W,W,L,W,W,W,W,W,W,W,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,Q,Q,Q,W,W,W,Q,U,Q,U,Q,F,X,U,W,Q,W,Q,W,W,W,W,U,Q,U,S,Q,W,W,W,W,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,U,F,U,W,W,W,Q,Q,Q,Q,Q,Q,X,G,W,Q,W,Q,W,W,W,W,Q,X,Q,W,Q,Q,Q,Q,Q,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,I,H,C,W,W,W,Q,U,Q,U,Q,U,Q,U,W,Q,W,Q,W,W,W,W,Q,Q,Q,W,W,W,Q,R,Q,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,W,W,W,W,Q,U,Q,U,Q,U,Q,U,W,Q,B,Q,W,W,W,W,X,X,X,W,W,W,Q,Q,Q,W,W,W,Q,W,T,T,W,M,
            W,T,T,W,W,Q,W,W,Q,Q,Q,W,W,W,Q,Q,X,Q,Q,Q,Q,B,W,Q,W,Q,W,W,W,W,Q,Q,Q,W,W,W,W,W,W,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,Q,Q,Q,W,W,W,Q,U,Q,U,Q,U,F,U,W,Q,W,Q,W,W,W,W,Q,X,Q,Q,Q,Q,Q,Q,Q,Q,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,X,X,X,W,W,W,Q,U,Q,U,F,U,F,U,W,Q,W,Q,W,W,W,W,Q,Q,Q,W,W,W,W,W,W,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,I,H,I,W,W,W,Q,Q,C,G,F,F,H,I,W,F,W,F,W,W,W,W,Q,H,V,W,W,W,W,W,W,W,W,W,Q,W,T,T,W,W,
            W,T,T,W,W,Q,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,H,W,H,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,Q,W,W,T,W,W,
            W,T,T,W,Q,Q,Q,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,Q,Q,Q,W,T,W,M,
            W,T,Q,W,F,F,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,F,F,W,T,W,W,
            W,T,T,W,H,F,Q,W,M,W,W,W,W,M,W,W,W,W,W,W,W,W,W,W,M,W,W,W,W,W,W,W,W,M,W,W,W,W,W,W,W,Q,F,H,S,T,W,W,
            W,T,T,W,W,W,W,W,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,W,W,W,W,W,T,W,W,
            W,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,T,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W
    };




    public static final int[] SOKOBAN_TELEPORT_LEVEL =	{
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	R, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	H, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	F, 	H, 	H, 	F, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, Q, 	X, 	H, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	F, 	F, 	F, 	F, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	B, 	F, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	G, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	X, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	C, 	R, 	S, Q, 	L, 	B, 	W, 	F, 	R, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	R, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	I, 	F, 	R, 	W, 	W, 	W, 	W, 	F, 	H, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	F, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	A, 	X, Q, 	W, 	W, 	W, 	W, 	W,
            W, 	W, Q, 	F, 	F, 	W, 	W, 	W, 	W, 	F, 	F, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	F, 	F, Q, 	W, 	W, 	W, 	W, 	W,
            W, 	W, Q, Q, Q, 	W, 	W, 	W, 	W, Q, 	X, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	R, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	F, 	F, 	R, 	W, 	W, 	W, 	W, 	W,
            W, 	W, Q, Q, 	H, 	W, 	W, 	W, 	W, Q, 	R, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	H, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	R, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	C, 	G, 	G, 	G, 	G, 	F, 	F, 	F, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, 	X, 	A, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	V, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	X, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	W, 	W, Q, Q, Q, Q, 	W, 	W, Q, Q, Q, 	I, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	E, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	C, 	W, Q, 	V, 	X, Q, 	W, 	W, Q, 	X, 	V, Q, 	W, 	W, 	W, 	W,
            W, 	W, Q, 	R, 	W, 	W, 	W, Q, 	R, 	X, Q, 	W, 	W, 	W, 	W, 	W, Q, 	R, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	G, 	W, 	W, Q, Q, Q, 	R, 	W, 	W, 	R, Q, Q, Q, 	W, 	W, 	W, 	W,
            W, 	W, Q, 	X, 	W, 	W, 	W, Q, Q, 	I, Q, 	W, 	W, 	W, 	W, 	W, Q, 	X, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	C, 	G, 	W, 	W, 	W, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	F, 	F, 	F, 	W, 	W, 	W, 	W, 	H, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	C, 	G, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	H, Q, 	W, 	W, 	W, 	W, 	S, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, Q, Q, Q, 	R, 	W, 	W, 	R, Q, Q, Q, 	W, 	W, 	W, 	W,
            W, 	W, 	C, 	G, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	R, Q, 	U, 	F, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, Q, 	V, 	X, Q, 	W, 	W, Q, 	X, 	V, Q, 	W, 	W, 	W, 	W,
            W, 	W, 	U, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	A, 	R, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	I, Q, Q, Q, 	W, 	W, Q, Q, Q, 	H, 	W, 	W, 	W, 	W,
            W, 	W, 	U, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	U, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	C, 	R, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	U, Q, Q, Q, Q, Q, 	W, Q, Q, 	W, 	W, Q, 	X, Q, 	W, 	W, 	W, 	W, 	W, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	U, Q, 	U, Q, Q, Q, 	L, Q, 	H, 	W, 	W, 	G, 	G, 	G, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	U, 	I, 	U, 	I, Q, Q, 	W, Q, Q, 	W, 	W, Q, Q, 	I, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	L, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	L, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	H, Q, Q, Q, Q, 	F, 	F, 	F, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, 	R, 	H, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	I, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	U, Q, Q, Q, 	U, Q, 	U, Q, Q, 	U, Q, 	U, Q, Q, Q, Q, Q, Q, 	U, Q, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, 	U, Q, 	U, Q, Q, 	U, Q, 	U, Q, Q, Q, Q, Q, Q, Q, Q, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	U, Q, 	U, 	U, 	U, Q, 	U, 	F, 	F, 	U, Q, 	U, 	U, 	U, Q, Q, Q, Q, 	U, Q, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	F, 	F, 	F, 	F, 	F, 	F, 	F, 	F, 	F, 	F, 	F, 	F, Q, Q, Q, Q, Q, Q, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	L, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	U, 	G, 	U, 	F, Q, Q, Q, Q, Q, Q, Q, Q, 	F, 	U, 	G, Q, Q, Q, 	U, 	I, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	F, 	F, 	F, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, 	G, 	F, Q, Q, 	X, Q, 	X, 	X, Q, 	X, Q, Q, 	F, 	G, Q, Q, Q, Q, Q, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	U, 	G, 	U, 	F, 	V, Q, 	V, Q, Q, 	V, Q, 	V, 	F, 	U, 	G, Q, Q, Q, 	U, Q, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, 	H, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	F, 	F, Q, Q, Q, Q, Q, Q, Q, Q, 	F, 	F, Q, Q, Q, Q, Q, Q, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	U, Q, 	U, 	U, 	U, 	U, 	U, 	U, 	U, 	U, 	U, 	U, 	U, 	U, Q, Q, Q, Q, 	U, Q, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, Q, Q, 	X, Q, Q, 	X, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	I, 	U, Q, Q, Q, Q, Q, 	X, Q, Q, 	X, Q, Q, Q, Q, Q, Q, Q, Q, 	U, 	I, 	W, 	W,
            W, 	W, 	W, Q, Q, 	H, 	I, 	H, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, 	V, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, 	W, 	W,
            W, 	W, 	W, 	F, 	F, 	F, 	F, 	F, 	F, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	U, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, 	U, Q, 	W, 	W,
            W, 	W, 	W, 	F, 	F, 	F, 	F, 	F, 	F, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	H, Q, 	W, 	W, 	W, 	W, 	U, 	U, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, 	W, 	W,
            W, 	W, 	W, Q, 	X, 	X, 	R, 	C, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	H, Q, Q, Q, 	R, 	S, Q, 	U, Q, 	U, Q, 	U, Q, 	U, Q, 	U, Q, 	U, Q, 	U, Q, 	U, Q, 	U, Q, 	U, Q, 	W, 	W,
            W, 	W, 	W, Q, Q, 	G, 	G, 	G, 	I, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	H, Q, 	W, 	W, 	W, 	W, 	H, 	U, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, Q, 	H, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W


    };

    public static final int[] SOKOBAN_PUZZLE_LEVEL =	{
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, 	R, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, Q, Q, Q, Q, 	U, 	S, 	R, 	W, 	W, 	W, Q, 	A, Q, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	C, 	C, 	U, 	H, 	U, 	C, 	C, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, 	H, 	H, Q, Q, 	W, 	W, 	W, 	W, 	W, 	U, 	I, 	U, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	G, 	G, 	U, 	F, 	U, 	G, 	G, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, 	H, 	H, Q, Q, 	W, 	W, 	W, 	W, 	W, 	U, 	I, 	U, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, 	S, Q, 	G, 	G, 	U, 	F, 	U, 	G, 	G, 	V, 	S, Q, Q, 	U, 	W, 	W, 	W, 	W, 	W, 	W, 	U, Q, Q, Q, Q, 	U, 	W, 	W, 	W, 	W, 	W, 	U, 	I, 	U, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	G, 	U, 	W, 	W, 	C, 	I, 	U, 	F, 	U, 	I, 	C, 	W, 	W, 	W, 	W, 	G, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	I, 	U, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	V, 	W, 	W, 	G, 	G, 	U, 	F, 	U, 	G, 	G, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	I, 	U, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	C, Q, 	U, 	F, 	U, Q, 	C, 	W, 	W, 	W, 	W, 	G, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	I, 	U, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	S, 	R, 	G, 	G, 	U, 	F, 	U, 	G, 	G, 	V, 	S, Q, Q, 	U, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	I, 	U, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, 	A, 	E, 	A, Q, 	I, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	I, 	U, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	G, 	C, 	U, 	C, 	G, 	U, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	H, 	U, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	L, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	H, Q, Q, Q, 	W, 	B, Q, Q, Q, 	B, 	W, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	F, 	F, 	F, 	W, 	G, 	G, Q, 	G, 	G, 	W, Q, Q, 	F, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, 	R, 	W, 	W, 	W, Q, Q, 	U, Q, 	H, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	H, Q, Q, Q, 	S, Q, Q, 	G, Q, Q, 	S, Q, 	G, 	F, 	H, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	H, Q, 	W, 	W, 	W, Q, Q, Q, Q, 	H, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	F, 	F, 	F, 	W, Q, 	F, 	F, 	F, Q, 	W, Q, Q, 	F, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	W, 	W, 	W, Q, Q, 	U, Q, 	H, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	H, Q, Q, Q, 	W, Q, 	F, 	H, 	F, Q, 	W, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	R, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	L, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	C, 	G, 	C, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	X, 	G, 	X, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, Q, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	C, 	C, 	C, 	C, 	C, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	I, Q, 	G, 	G, 	G, 	G, 	G, Q, 	I, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	F, 	H, 	H, Q, 	L, Q, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, 	U, 	U, 	U, 	U, 	U, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	H, Q, 	W, 	F, 	U, 	U, 	U, 	U, 	W, 	T, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	C, 	G, 	U, 	F, Q, Q, Q, 	F, 	U, 	G, 	C, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	W, 	A, 	X, 	B, 	I, Q, 	W, 	T, 	W, 	W, 	W, Q, Q, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	C, 	G, 	U, Q, 	F, 	F, 	F, Q, 	U, 	G, 	C, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	W, Q, Q, Q, Q, Q, 	W, 	T, 	W, 	W, 	W, 	U, 	A, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	C, 	G, 	U, Q, 	U, 	F, 	U, Q, 	U, 	G, 	C, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, Q, Q, Q, Q, Q, 	W, 	T, 	W, 	W, 	W, 	T, 	T, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	C, 	G, 	U, Q, 	U, 	F, 	U, Q, 	U, 	G, 	C, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	L, 	W, 	W, 	W, 	W, 	W, 	T, 	W, 	W, 	W, 	T, 	F, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	C, 	G, 	U, 	F, 	U, 	H, 	U, 	F, 	U, 	G, 	C, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, 	A, 	F, 	F, Q, 	W, 	W, 	W, 	T, 	W, 	W, 	W, 	T, 	F, 	R, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	F, 	U, 	U, 	U, 	U, 	U, 	F, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	G, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	T, 	W, 	W, 	W, 	T, 	F, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	H, 	F, 	S, Q, 	B, Q, 	B, Q, 	B, Q, 	S, 	F, 	H, 	W, 	W, 	W, 	W, 	W, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	T, 	W, 	W, 	W, 	T, 	T, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	L, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	T, 	W, 	W, 	W, 	T, 	T, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	G, 	V, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	T, 	T, 	T, 	T, 	T, 	T, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	B, 	G, 	G, 	G, 	G, 	G, 	F, 	F, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	C, 	C, 	G, 	C, 	C, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, Q, Q, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	U, 	G, 	U, 	U, 	W, 	W, 	W, 	W, 	W, Q, Q, 	R, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	C, 	C, Q, 	C, 	C, 	W, 	W, 	W, 	W, 	W, 	X, Q, Q, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	G, Q, 	G, 	U, 	W, 	W, 	W, 	W, 	W, 	F, Q, Q, 	W, 	W, 	I, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	B, 	C, Q, 	C, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	U, 	U, Q, 	U, 	U, 	W, 	W, 	W, 	W, Q, Q, Q, 	W, 	W, 	W, 	G, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	B, 	F, 	F, 	F, 	F, 	F, 	F, 	F, 	L, Q, 	B, Q, 	F, 	H, 	D, 	C, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, Q, Q, Q, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
            W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W, 	W,
    };


}
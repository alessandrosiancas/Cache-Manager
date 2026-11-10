package datastructures;

public class RBTree {
    private RBNode root;
    private RBNode nil; // nodo sentinella (nero, foglia)
    public int treeSize;

    public RBTree() {
        // Inizializzazione nodo sentinella
        nil = new RBNode("NIL");
        nil.color = false;
        nil.left = null;
        nil.right = null;
        nil.parent = null;

        // Albero vuoto (c'è solo la sentinella)
        root = nil;
        treeSize = 0;
    }

    // Ricerca binaria di una risorsa (es. "d")
    public RBNode search(String k) {
        RBNode x = this.root;

        while (x != nil && !k.equals(x.key)) {
            if (k.compareTo(x.key) < 0) // k<x.key
                x = x.left;
            else // k>=x.key
                x = x.right;
        }
        return x;
    }

    // Inserimento standard da ABR (con nil come sentinella)
    public void insert(RBNode z) {
        RBNode y = nil;
        RBNode x = root;

        while (x != nil) {
            y = x;
            if (z.key.compareTo(x.key) < 0) // z.key<x.key
                x = x.left;
            else
                x = x.right;
        }

        z.parent = y;
        if (y == nil) // albero vuoto
            root = z;
        else if (z.key.compareTo(y.key) < 0)
            y.left = z;
        else
            y.right = z;

        z.left = nil;
        z.right = nil;
        z.color = true; // true=RED

        treeSize++;
        insertFixup(z); // sistemazione delle violazioni
    } // O(log k)

    private void insertFixup(RBNode z) {
        while (z.parent.color) { // z.parent.color==RED
            if (z.parent == z.parent.parent.left) { // se il padre di z è figlio sinistro
                RBNode y = z.parent.parent.right; // y è zio di z
                if (y.color) { // Caso 1
                    z.parent.color = false;
                    y.color = false;
                    z.parent.parent.color = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) { // Caso 2 (mi riconduco al 3)
                        z = z.parent;
                        leftRotate(z);
                    }
                    // Caso 3
                    z.parent.color = false;
                    z.parent.parent.color = true;
                    rightRotate(z.parent.parent);
                }
            } else { // 3 casi specchiati, invertendo left e right
                RBNode y = z.parent.parent.left;
                if (y.color) { // y.color==RED
                    z.parent.color = false;
                    y.color = false;
                    z.parent.parent.color = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = false;
                    z.parent.parent.color = true;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = false;
    } // O(log k)

    // Rimozione di un nodo
    public void delete(RBNode z) {
        RBNode y = z;
        RBNode x;
        boolean yOriginalColor = y.color;
        if (z.left == nil) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == nil) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z)
                x.parent = y;
            else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        treeSize--;
        if (!yOriginalColor)
            deleteFixup(x);
    } // O(log k) con la fixup

    private void deleteFixup(RBNode x) {
        while (x != root && !x.color) {
            if (x == x.parent.left) {
                RBNode w = x.parent.right;
                if (w.color) {
                    w.color = false;
                    x.parent.color = true;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (!w.left.color && !w.right.color) {
                    w.color = true;
                    x = x.parent;
                } else {
                    if (!w.right.color) {
                        w.left.color = false;
                        w.color = true;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = false;
                    w.right.color = false;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                RBNode w = x.parent.left;
                if (w.color) {
                    w.color = false;
                    x.parent.color = true;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (!w.left.color && !w.right.color) {
                    w.color = true;
                    x = x.parent;
                } else {
                    if (!w.left.color) {
                        w.right.color = false;
                        w.color = true;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = false;
                    w.left.color = false;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = false;
    } // O(log k)

    private RBNode minimum(RBNode x) {
        while (x.left != nil)
            x = x.left;
        return x;
    } // O(log k)

    // Rotazione a sinistra attorno al nodo x
    private void leftRotate(RBNode x) {
        RBNode y = x.right;
        x.right = y.left;
        if (y.left != nil)
            y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == nil)
            root = y;
        else if (x == x.parent.left)
            x.parent.left = y;
        else
            x.parent.right = y;
        y.left = x;
        x.parent = y;
    } // Θ(1)

    // Rotazione a destra attorno al nodo y
    private void rightRotate(RBNode y) {
        RBNode x = y.left;
        y.left = x.right;
        if (x.right != nil)
            x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == nil)
            root = x;
        else if (y == y.parent.right)
            y.parent.right = x;
        else
            y.parent.left = x;
        x.right = y;
        y.parent = x;
    } // Θ(1)

    private void transplant(RBNode u, RBNode v) {
        if (u.parent == nil)
            root = v;
        else if (u == u.parent.left)
            u.parent.left = v;
        else
            u.parent.right = v;
        v.parent = u.parent;
    } // Θ(1)

    public RBNode getRoot() {
        return root;
    } // Θ(1)

    public RBNode getNil() {
        return nil;
    } // Θ(1)
}
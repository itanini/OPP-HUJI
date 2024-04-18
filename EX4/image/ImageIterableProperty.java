package image;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
class ImageIterableProperty<T> implements Iterable<T> {
    private final Image img;
    private final BiFunction<Integer, Integer, T> propertySupplier;

    private int jump = 1;

    public ImageIterableProperty(
            Image img,
            BiFunction<Integer, Integer, T> propertySupplier) {
        this.img = img;
        this.propertySupplier = propertySupplier;
    }

    /**
     * loaind on the normal constructor, that make the iterator jumo in a certain gaps
     * @param img the original image
     * @param propertySupplier the iterator
     * @param jump the gaps between iterations
     */
    public ImageIterableProperty(
            Image img,
            BiFunction<Integer, Integer, T> propertySupplier, int jump) {
        this.img = img;
        this.propertySupplier = propertySupplier;
        this.jump = jump;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int x = 0, y = 0;

            @Override
            public boolean hasNext() {
                return y < img.getHeight();
            }

            @Override
            public T next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                var next = propertySupplier.apply(x, y);
                x += jump;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += jump;
                }
                return next;
            }
        };
    }
}

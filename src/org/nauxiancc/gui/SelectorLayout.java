package org.nauxiancc.gui;

import javax.swing.*;
import java.awt.*;

/**
 * FlowLayout subclass that fully supports wrapping of components.
 *
 * @author Naux
 * @version 1.0
 * @since 1.02
 */
public class SelectorLayout extends FlowLayout {

    /**
     * Creates a new flow layout manager with the indicated alignment
     * and the indicated horizontal and vertical gaps.
     * <p/>
     * <p/>
     * The value of the alignment argument must be one of
     * <code>FlowLayout.LEFT</code>, <code>FlowLayout.RIGHT</code>,
     * <code>FlowLayout.CENTER</code>, <code>FlowLayout.LEADING</code>,
     * or <code>FlowLayout.TRAILING</code>.
     *
     * @param align the alignment value
     * @param hgap  the horizontal gap between components
     * @param vgap  the vertical gap between components
     */
    public SelectorLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    /**
     * Returns the preferred dimension needed to layout the target
     * container.
     *
     * @param target target to get layout size for
     * @return the dimension to layout the target container
     */
    @Override
    public Dimension preferredLayoutSize(final Container target) {
        synchronized (target.getTreeLock()) {
            final int targetWidth = (target.getSize().width == 0) ? Integer.MAX_VALUE : target.getSize().width;
            final int hgap = getHgap();
            final int vgap = getVgap();
            final Dimension dim = new Dimension(0, 0);
            final Insets insets = target.getInsets();
            final Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, target);
            final int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
            final int maxWidth = targetWidth - horizontalInsetsAndGap;

            int rowWidth = 0;
            int rowHeight = 0;

            for (int i = 0; i < target.getComponentCount(); i++) {
                final Component comp = target.getComponent(i);

                if (comp.isVisible()) {
                    final Dimension d = comp.getPreferredSize();

                    if (rowWidth + d.width > maxWidth) {
                        dim.width = Math.max(dim.width, rowWidth);

                        if (dim.height > 0) {
                            dim.height += getVgap();
                        }

                        dim.height += rowHeight;
                        rowWidth = 0;
                        rowHeight = 0;
                    }

                    if (rowWidth != 0) {
                        rowWidth += hgap;
                    }

                    rowWidth += d.width;
                    rowHeight = Math.max(rowHeight, d.height);
                }
            }

            dim.width = Math.max(dim.width, rowWidth);

            if (dim.height > 0) {
                dim.height += getVgap();
            }

            dim.height += rowHeight;
            dim.width += horizontalInsetsAndGap;
            dim.height += insets.top + insets.bottom + vgap * 2;

            if (scrollPane != null && target.isValid()) {
                dim.width -= (hgap + 1);
            }

            return dim;
        }
    }
}
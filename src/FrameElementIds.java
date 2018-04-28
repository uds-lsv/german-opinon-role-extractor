import salsa.corpora.elements.Frame;
import salsa.corpora.noelement.Id;

/**
 * @author Erik Hahn
 *
 * This class creates unique IDs for {@link salsa.corpora.elements.FrameElement} objects. It is similar
 * to {@link FrameIds}.
 */
class FrameElementIds {
    private final Frame frame;
    private int feCount = 0;

    public FrameElementIds(Frame frame) {
        this.frame = frame;
    }

    public Id next() {
        feCount++;
        return new Id(frame.getId().getId() + "_e" + feCount);
    }
}

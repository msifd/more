package msifeed.mc.aorta.client.lock;

import msifeed.mc.aorta.locks.LockObject;
import msifeed.mc.aorta.locks.LocksRpc;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.input.TextInput;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.tileentity.TileEntity;

public class ScreenSkeletalKey extends MellowGuiScreen {
    private final LockObject lock;

    public ScreenSkeletalKey(LockObject lock) {
        this.lock = lock;
        final Window window = new Window();
        window.setTitle("Skeletal Key");
        scene.addChild(window);

        final Widget content = window.getContent();

        final ButtonLabel toggleBtn = new ButtonLabel("Toggle lock");
        toggleBtn.getSizeHint().x = 20;
        toggleBtn.setClickCallback(() -> {
            lock.toggleLocked();
            sendOverrideRequest();
        });
        content.addChild(toggleBtn);

        final TextInput diffInput = new TextInput();
        diffInput.getSizeHint().x = 20;
        diffInput.setText(String.valueOf(lock.getDifficulty()));
        diffInput.setFilter(TextInput::isUnsignedInt);
        content.addChild(diffInput);

        final ButtonLabel diffBtn = new ButtonLabel("Set diff");
        diffBtn.getSizeHint().x = 20;
        diffBtn.setClickCallback(() -> {
            final int d = diffInput.getInt();
            if (d > 0 && d <= 100) {
                lock.setDifficulty(d);
                sendOverrideRequest();
            }
        });
        content.addChild(diffBtn);
    }

    private void sendOverrideRequest() {
        final TileEntity te = lock.getTileEntity();
        Rpc.sendToServer(LocksRpc.gmOverrideLock, te.xCoord, te.yCoord, te.zCoord, lock.isLocked(), lock.getDifficulty());
    }
}

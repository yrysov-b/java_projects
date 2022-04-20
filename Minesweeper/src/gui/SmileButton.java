package gui;

import processing.core.PApplet;

public class SmileButton
{
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final float borderWidth = 8f;
    private final PApplet canvas;
    private boolean isSad;
    private boolean isVictorious = false;
    private OnClickListener listener;

    public SmileButton(PApplet canvas, float x, float y, float width, float height) {
        this.canvas = canvas;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.listener = null;
        this.isSad = false;

        drawUnpressed();
    }

    public void drawUnpressed() {
        canvas.pushMatrix();
        canvas.pushStyle();
        canvas.strokeWeight(0f);
        canvas.translate(this.x, this.y);

        canvas.fill(150,150,150);
        canvas.rect(borderWidth, borderWidth, width-16, height-16);


        // Draw the smile
        canvas.popStyle();
        canvas.popMatrix();

        canvas.pushMatrix();
        canvas.pushStyle();
        canvas.translate(this.x, this.y);
        canvas.fill(255, 255, 0);
        //draw the head
        canvas.strokeWeight(0f);
        canvas.circle(width/2f, height/2f, 50);
        canvas.strokeWeight(3f);

        if(isVictorious) {
            canvas.stroke(255,0,0);
            //red
            canvas.fill(255,0,0);
            //draw the eyes
            canvas.circle(width/2f-10f, height/2f-10f, 6);
            canvas.circle(width/2f+10f, height/2f-10f, 6);
        } else {
            //white
            canvas.fill(0);
            //draw the eyes
            canvas.circle(width/2f-10f, height/2f-10f, 3);
            canvas.circle(width/2f+10f, height/2f-10f, 3);
        }
        canvas.fill(255, 255, 0);
        if(!isSad) {
            canvas.arc(width/2f, height/2f+7f, 25, 18, 0, canvas.PI);
        } else {
            canvas.arc(width/2f, height/2f+13f, 25, 18, -canvas.PI, 0);
        }

        canvas.popStyle();
        canvas.popMatrix();
    }

    public void drawPressed() {
        // 3D button
        canvas.pushMatrix();
        canvas.pushStyle();
        canvas.strokeWeight(0f);
        canvas.translate(this.x, this.y);

        canvas.fill(100,100,100);
        canvas.rect(borderWidth, borderWidth, width-16, height-16);

        canvas.popStyle();
        canvas.popMatrix();

        // Smile
        canvas.pushMatrix();
        canvas.pushStyle();

        canvas.translate(this.x, this.y);
        canvas.fill(255, 255, 0);
        //draw the head
        canvas.strokeWeight(0f);
        canvas.circle(width/2f+2f, height/2f+2f, 50);

        canvas.fill(0);
        canvas.strokeWeight(3f);
        //white
        //draw the eyes
        canvas.circle(width/2f-10f+2f, height/2f-10f+2f, 3);
        canvas.circle(width/2f+10f+2f, height/2f-10f+2f, 3);
        //draw the mouth
        canvas.fill(255, 255, 0);
        // strokeWeight(2f);
        if(!isSad) {
            canvas.arc(width/2f+2f, height/2f+7f+2f, 25, 18, 0, canvas.PI);
        } else {
            canvas.arc(width/2f+2f, height/2f+13f+2f, 25, 18, -canvas.PI, 0);
        }
        canvas.popStyle();
        canvas.popMatrix();
    }

    public void setIsSad(boolean isSad) {
        this.isSad = isSad;
        drawUnpressed();
    }

    public void setVictorious(boolean isVictorious) {
        this.isVictorious = isVictorious;
        drawUnpressed();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
        if(canvas.mousePressed) {
            if (canvas.mouseX > x && canvas.mouseX < x + width && canvas.mouseY > y && canvas.mouseY < y + height) {
                if(this.listener!=null) {
                    this.listener.onClick();
                    drawPressed();
                }
            }
        }
    }

    interface OnClickListener {
        void onClick();
    }
}
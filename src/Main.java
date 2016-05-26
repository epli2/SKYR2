import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.image.BufferedImage;

class Main extends JPanel {
    public static void main(String[] args) {
        JFrame frame1 = new JFrame("BasicPanel");
        JFrame frame2 = new JFrame("BasicPanel");
        frame1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame1.setSize(400,400);
        frame2.setSize(400,400);
        Panel panel1 = new Panel();
        Panel panel2 = new Panel();
        frame1.setContentPane(panel1);
        frame2.setContentPane(panel2);
        frame1.setVisible(true);
        frame2.setVisible(true);
        VideoCapture cap1 = new VideoCapture(0);
        VideoCapture cap2 = new VideoCapture(1);
        Mat webcam_image1 = new Mat();
        Mat webcam_image2 = new Mat();
        BufferedImage temp;

        if (cap1.isOpened()) {
            while (true) {
                cap1.read(webcam_image1);
                cap2.read(webcam_image2);
                if (!webcam_image1.empty() && !webcam_image2.empty()) {
                    Imgproc.resize(webcam_image1, webcam_image1, new Size(webcam_image2.width(), webcam_image2.height()));
                    Imgproc.resize(webcam_image2, webcam_image2, new Size(webcam_image2.width(), webcam_image2.height()));
                    frame1.setSize(webcam_image1.width() + 40, webcam_image1.height() + 60);
                    frame2.setSize(webcam_image2.width() + 40, webcam_image2.height() + 60);
                    temp = Panel.matToBufferedImage(webcam_image1);
                    panel1.setimage(temp);
                    temp = Panel.matToBufferedImage(webcam_image2);
                    panel2.setimage(temp);
                    panel1.repaint();
                    panel2.repaint();
                } else {
                    System.out.println(" --(!) No captured frame -- ");
                }
            }
        }
    }
}
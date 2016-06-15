import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.io.File;

import static org.opencv.core.Core.getTickCount;
import static org.opencv.core.Core.getTickFrequency;

class Main extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Main");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        Panel panel1 = new Panel();
        frame.setContentPane(panel1);
        frame.setVisible(true);
        VideoCapture cap1 = new VideoCapture(0);
        Mat im1 = new Mat();
        Mat im2 = new Mat();
        Mat im3 = new Mat();
        MatOfRect faces;
        final int RESIZE = 2;
        // fps表示
        double startTime = getTickCount();
        double freq = 1000 / getTickFrequency();
        int frameCount = 0;
        int oldCount = 0;
        double fps;

        if (cap1.isOpened()) {
            cap1.read(im1);
            Imgproc.resize(im1, im1, new Size(im1.width() / RESIZE, im1.height() / RESIZE));
            frame.setSize(im1.width() + 40, im1.height() + 60);
            while (true) {
                cap1.read(im1);
                cap1.read(im2);
                cap1.read(im3);
                if (!im1.empty() && !im2.empty() && !im3.empty()) {
                    Imgproc.resize(im1, im1, new Size(im1.width() / RESIZE, im1.height() / RESIZE));
                    Imgproc.resize(im2, im2, new Size(im2.width() / RESIZE, im2.height() / RESIZE));
                    Imgproc.resize(im3, im3, new Size(im3.width() / RESIZE, im3.height() / RESIZE));
                    System.out.println(judgeDead(im1, im2, im3));
                    faces = detectFace(im1);
                    for (Rect rect : faces.toArray()) {
                        Imgproc.rectangle(
                                im1,
                                new Point(rect.x, rect.y),
                                new Point(rect.x + rect.width, rect.y + rect.height),
                                new Scalar(0, 255, 0));
                    }
                    panel1.setimage(Panel.matToBufferedImage(im1));
                    panel1.repaint();
                } else {
                    System.out.println(" --(!) No captured frame -- ");
                }
                double nowTime = getTickCount();
                double diffTime = (nowTime - startTime) * freq;
                startTime = nowTime;
                fps = (frameCount - oldCount) / (diffTime / 1000.0);
                System.out.println("fps: " + fps);
                oldCount = frameCount;
                frameCount++;
            }
        }
    }

    static boolean judgeDead(Mat im1, Mat im2, Mat im3) {
        int sum = 0;
        final int threshold = im1.width() * im1.height();
        Mat d1 = new Mat();
        Mat d2 = new Mat();
        Mat diff = new Mat();
        Mat mask = new Mat();
        Mat im_mask = new Mat();

        Core.absdiff(im1, im2, d1);
        Core.absdiff(im2, im3, d2);
        Core.bitwise_and(d1, d2, diff);
        Imgproc.threshold(diff, mask, 5, 1, Imgproc.THRESH_BINARY);
        Imgproc.threshold(mask, im_mask, 0, 255, Imgproc.THRESH_BINARY);
        Imgproc.medianBlur(im_mask, im_mask, 5);
        Imgproc.cvtColor(im_mask, im_mask, Imgproc.COLOR_RGB2GRAY);
        for (int i = 0; i < im_mask.height(); i++) {
            for (int j = 0; j < im_mask.width(); j++) {
                double[] data = im_mask.get(i, j);
                sum += data[0];
            }
        }
        System.out.println("diff: " + sum);
        return sum > threshold;
    }

    static MatOfRect detectFace(Mat frame) {
        File cascadeFile = new File("/usr/local/Cellar/opencv3/3.1.0_3/share/OpenCV/haarcascades/haarcascade_frontalface_default.xml");
        MatOfRect faces = new MatOfRect();
        CascadeClassifier faceDetector =
                new CascadeClassifier(cascadeFile.getAbsolutePath());
//        faceDetector.detectMultiScale(frame, faces, 1.1, 3, 0, new Size(50, 50), new Size(1000, 1000));
        faceDetector.detectMultiScale(frame, faces);
        return faces;
    }
}
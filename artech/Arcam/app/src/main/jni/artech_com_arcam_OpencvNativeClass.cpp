#include <artech_com_arcam_OpencvNativeClass.h>

JNIEXPORT jint JNICALL Java_artech_com_arcam_OpencvNativeClass_convertGray
  (JNIEnv *, jclass, jlong addrRgba, jlong addrGray){
    Mat& mRgb = *(Mat*)addrRgba;
    Mat& mGray = *(Mat*)addrGray;

    int conv;
    jint retVal;
    //conv = toGray(mRgb, mGray);

    conv = means(mRgb, mGray);

    retVal = (jint)conv;

    return retVal;
}

int toGray(Mat img, Mat& gray){
    cvtColor(img, gray, CV_BGR2GRAY);
    if(gray.rows==img.rows && gray.cols==img.cols){
        return 1;
    }
    return 0;
}

int means(Mat img, Mat& kmeansImg) {

    Mat samples(img.total(), 3, CV_32F);

    auto samples_ptr = samples.ptr<float>(0);
    for (int row = 0; row != img.rows; ++row) {
        auto img_begin = img.ptr<uchar>(row);
        auto img_end = img_begin + img.cols * img.channels();
        //auto samples_ptr = samples.ptr<float>(row * src.cols);
        while (img_begin != img_end) {
            samples_ptr[0] = img_begin[0];
            samples_ptr[1] = img_begin[1];
            samples_ptr[2] = img_begin[2];
            samples_ptr += 3; img_begin += 3;
        }
    }

    int clusterCount = 5;
    Mat labels;
    int attempts = 3;
    Mat centers;
    kmeans(samples, clusterCount, labels, TermCriteria(CV_TERMCRIT_ITER | CV_TERMCRIT_EPS, 10, 0.01), attempts, KMEANS_PP_CENTERS, centers);


    Mat new_image(img.size(), img.type());
    for (int row = 0; row != img.rows; ++row) {
        auto new_image_begin = new_image.ptr<uchar>(row);
        auto new_image_end = new_image_begin + new_image.cols * 3;
        auto labels_ptr = labels.ptr<int>(row * img.cols);

        while (new_image_begin != new_image_end) {
            int const cluster_idx = *labels_ptr;
            auto centers_ptr = centers.ptr<float>(cluster_idx);
            new_image_begin[0] = centers_ptr[0];
            new_image_begin[1] = centers_ptr[1];
            new_image_begin[2] = centers_ptr[2];
            new_image_begin += 3; ++labels_ptr;
        }
    }


   new_image.copyTo(kmeansImg);

    return 0;
}
#include <jni.h>
#include <opencv2/opencv.hpp>

using namespace cv;

extern "C"
JNIEXPORT void JNICALL
Java_com_autorecotest_ict_autorecotest_MainActivity_ConvertRGBtoGray(JNIEnv *env, jobject instance,
                                                                     jlong matAddrInput,
                                                                     jlong matAddrResult) {
    // 입력 RGBA 이미지를 GRAY 이미지로 변환
    Mat &matInput = *(Mat *)matAddrInput;
    Mat &matResult = *(Mat *)matAddrResult;
    cvtColor(matInput, matResult, CV_RGBA2GRAY);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_autorecotest_ict_autorecotest_MainActivity_imageprocessing(JNIEnv *env, jobject instance,
                                                            jlong inputImage,
                                                            jlong outputImage) {
    // TODO
    Mat &img_input = *(Mat *) inputImage;
    Mat &img_output = *(Mat *) outputImage;

    //앱으로 전처리 시 *** 표시된 것만 사용하기

    cvtColor(img_input, img_input, CV_BGR2RGB);    //원본 이미지 (->원본으로) ***
    cvtColor(img_input, img_output, CV_BGR2RGB);   //리턴 이미지 (->원본으로)

    //cvtColor(img_input, img_output, CV_RGB2GRAY);  //리턴 이미지 (->흑백으로)  ***

    //GaussianBlur(img_output, img_output, Size(3, 3), 0);  //윤곽선을 더 잘 잡기 위해 가우시안 필터 적용
    //adaptiveThreshold(img_output, img_output, 255, CV_ADAPTIVE_THRESH_MEAN_C ,CV_THRESH_BINARY, 99, 0); //글씨 검정색, 배경 흰색

    //adaptiveThreshold(img_output, img_output, 255, CV_ADAPTIVE_THRESH_MEAN_C ,CV_THRESH_BINARY_INV, 391, 65); //글씨 검정색, 배경 흰색 ***

}
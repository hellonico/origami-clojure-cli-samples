(require '[opencv4.core :refer [CV_8UC1 dump]])
(import '[org.opencv.core Mat])

(dump (Mat/eye 3 3 CV_8UC1))
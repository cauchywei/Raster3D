import com.cauchywei.raster3d.math.Matrix
import com.cauchywei.raster3d.math.Vec
import com.cauchywei.raster3d.math.matrixOf

/**
 * Created by cauchywei on 2017/6/8.
 */
class Camera(original: Vec,
             direction: Vec,
             focalLen: Double,
             n: Double,
             f: Double,
             l: Double,
             r: Double,
             t: Double,
             b: Double) {


    val original: Vec = original
    val direction: Vec = direction
    val frustumMatrix: Matrix = matrixOf(4, 4,
            2 * n / (r - l), 0.0, (r + l) / (r - l), 0.0,
            0.0, 2 * n / (t - b), (t + b) / (t - b), 0.0,
            0.0, 0.0, -(f + n) / (f - n), -2 * n * f / (f - n),
            0.0, 0.0, -1.0, 0.0)


}
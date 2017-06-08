import com.cauchywei.raster3d.math.Vec
import com.cauchywei.raster3d.math.vec4Of

/**
 * Created by cauchywei on 2017/6/8.
 */

typealias Position = Vec

class Renderer(camera: Camera = Camera(
        original = vec4Of(),
        direction = vec4Of(z = -1.0),
        focalLen = 5.0,
        n = -2.0,
        f = -10.0,
        l = -5.0,
        r = 5.0,
        t = 5.0,
        b = 5.0
)) {

    var camera = camera



}

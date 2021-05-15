package greenpixel.math;

public class Utils
{
	public static boolean isAngleInRange(double theta, double startAngle, double endAngle)
	{
		double thetaConvert = (theta > 0) ? theta : theta + 2 * Math.PI;
		double endAngleConvert = (endAngle > 0) ? endAngle : endAngle + 2 * Math.PI;
		double startAngleConvert = (startAngle > 0) ? startAngle : startAngle + 2 * Math.PI;

		if (startAngleConvert < endAngleConvert)
		{
			return (thetaConvert >= startAngleConvert && thetaConvert <= endAngleConvert);
		}
		else
		{
			return (thetaConvert >= startAngleConvert || thetaConvert <= endAngleConvert);
		}
	}
}

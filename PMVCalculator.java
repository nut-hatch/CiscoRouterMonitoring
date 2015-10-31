import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Class that provides a calculation of the PMV and PPD value regarding ISO 7730.
 * PMV = Predicted Mean Vote
 * PPD = Predict percent of Dissatisfaction
 */
public class PMVCalculator {
	
	/**
	 * metabolic rate, in watts per square metre (W/m2)
	 */
	double metabolic_rate = 1.2;
	
	/**
	 * effective mechanical power, in watts per square metre (W/m2)
	 */
	double water_vapour_partial_pressure;
	
	/**
	 * clothing insulation, in square metres kelvin per watt (m2 K/W)
	 * 1.0 for winter.
	 */
	double clothing_insulation = 1.0;
	
	/**
	 * relative air velocity, in metres per second (m/s)
	 */
	double relative_air_velocity = 0.1;
	
	/**
	 * mean radiant temperature, in degrees Celsius (°C)
	 */
	double mean_radiant_temperature;
	
	/**
	 * External work, normally around 0
	 */
	double external_work = 0;

	public PMVCalculator() {
		
	}
	
	/**
	 * Calculates PMV value of given temperature and humidity.
	 * @param temperature
	 * @param humidity
	 * @return
	 */
	public double calculatePmv (double temperature, double humidity) {
		mean_radiant_temperature = temperature;
		water_vapour_partial_pressure = humidity * 10 * FNPS(temperature);
		double icl = 0.155 * clothing_insulation;
		double m = metabolic_rate * 58.15;
		double w = external_work * 58.15;
		double mw = m-w;
		double fcl;
		if (icl < 0.078) {
			fcl = 1+1.29*icl;
		} else {
			fcl = 1.05+0.645 * icl;
		}
		
		double hcf = 12.1 * Math.sqrt(relative_air_velocity);
		double taa = temperature + 273;
		double tra = mean_radiant_temperature + 273;
		
		double tcla = taa + (35.5 - temperature) / (3.5 * (6.45 * icl + 0.1));
		double P1 = icl * fcl;
		double P2 = P1 * 3.96;
		double P3 = P1 * 100;
		double P4 = P1 * taa;
		double P5 = 308.7 - 0.028 * mw + P2 * Math.pow((tra/100), 4);
		
		double xn = tcla / 100;
		double xf = xn;
		double n = 0;
		double eps = 0.00015;

		double hc = 0;
		double hcn;
		do {
			xf = (xf + xn) / 2;
			hcn = 2.38 * Math.pow(Math.abs(100* xf - taa), 0.25);
			if ( hcf > hcn) {
				hc = hcf;
			} else {
				hc = hcn;
			}
			xn = (P5 + P4 * hc - P2 * Math.pow(xf, 4)) / (100 + (P3 * hc));
			n = n + 1;
			if (n > 150) {
				return 99999;
			}
		} while (Math.abs(xn - xf) > eps);
		
		double tcl = 100 * xn - 273;
		
		double HL1 = 3.05 * 0.001 * (5733-6.99*mw-water_vapour_partial_pressure);
		double HL2 = 0;
		if (mw > 58.15) {
			HL2 = 0.42 * (mw - 58.15);
		}
		double HL3 = 1.7 * 0.00001 * m * (5867 - water_vapour_partial_pressure);
		double HL4 = 0.0014 * m * (34 - temperature);
		double HL5 = 3.96 * fcl * (Math.pow(xn,4) - (Math.pow(tra/100, 4)));
		double HL6 = fcl * hc * (tcl - temperature);
		
		double ts = 0.303 * Math.pow(Math.E,-0.036 * m) + 0.028;
		double pmv = ts * (mw-HL1-HL2-HL3-HL4-HL5-HL6);
		
		return pmv;
//		return new BigDecimal(pmv).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	
	/**
	 * Calculates the PPD of a PMV.
	 * @param pmv
	 * @return
	 */
	public double calculatePpd(double pmv) {
		return (100 - 95 * Math.pow(Math.E, -0.03353 * Math.pow(pmv, 4) - 0.2179 * Math.pow(pmv, 2)));
	}

	/**
	 * Calculates saturated vapour pressure of temperature.
	 * @param T
	 * @return
	 */
	private double FNPS (double T) {
		return  Math.pow(Math.E, 16.6536-4030.183/(T+235));
	}
}

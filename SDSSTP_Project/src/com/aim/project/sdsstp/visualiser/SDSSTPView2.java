package com.aim.project.sdsstp.visualiser;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.aim.project.sdsstp.AIM_SDSSTP;
import com.aim.project.sdsstp.instance.SDSSTPLocation;
import com.aim.project.sdsstp.interfaces.SDSSTPInstanceInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import com.aim.project.sdsstp.solution.SDSSTPSolution;

public class SDSSTPView2 extends JFrame implements SDSSTPViewerInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7066509516892991728L;

	private SDSSTPPanel oPanel;

	private Color oCitiesColor;

	private Color oRoutesColor;

	public SDSSTPView2(SDSSTPInstanceInterface oInstance, AIM_SDSSTP oProblem, Color oCitiesColor, Color oRoutesColor) {

		this.oCitiesColor = oCitiesColor;
		this.oRoutesColor = oRoutesColor;
		this.oPanel = new SDSSTPPanel(oInstance, oProblem);
		JFrame frame = new JFrame();
		frame.setTitle("SDSSTP Solution Visualiser");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(3);
		frame.add(this.oPanel);
		frame.setVisible(true);
	}

	class SDSSTPPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1295525707913147839L;

		private SDSSTPInstanceInterface instance;

		private AIM_SDSSTP oProblem;

		public SDSSTPPanel(SDSSTPInstanceInterface instance, AIM_SDSSTP oProblem) {

			this.instance = instance;
			this.oProblem = oProblem;
		}

		public void updateSolution(SDSSTPSolutionInterface[] current, SDSSTPSolutionInterface[] candidate, SDSSTPSolutionInterface best) {

			this.repaint();
		}

		LinkedList<Color> oColorStack = new LinkedList<Color>();

		private void drawHouse(Graphics g, int x, int y, int width, int height) {

			oColorStack.push(g.getColor());

			g.setColor(new Color(240, 180, 180));
			g.fillRect(x+width, y+height, -width, -height);

			g.setColor(Color.WHITE);

			int HALF_WIDTH = width / 2;
			int THIRD_WIDTH = width / 3;
			int TWOTHIRDS_WIDTH = THIRD_WIDTH * 2;
			
			int QUARTER_HEIGHT = height / 4;
			int HALF_HEIGHT = height / 2;
			// 12 x 12
			g.drawLine(x, y + QUARTER_HEIGHT, x + HALF_WIDTH, y);
			g.drawLine(x + HALF_WIDTH, y, x + width, y + QUARTER_HEIGHT);
			g.drawLine(x + width, y + QUARTER_HEIGHT, x, y + QUARTER_HEIGHT);
			g.drawLine(x, y + QUARTER_HEIGHT, x, y + height);
			g.drawLine(x, y + height, x + width, y + height);
			g.drawLine(x + width, y + height, x + width, y + QUARTER_HEIGHT);
			// door
			g.drawLine(x + THIRD_WIDTH, y + height, x + THIRD_WIDTH, y + HALF_HEIGHT);
			g.drawLine(x + THIRD_WIDTH, y + HALF_HEIGHT, x + TWOTHIRDS_WIDTH, y + HALF_HEIGHT);
			g.drawLine(x + TWOTHIRDS_WIDTH, y + height, x + TWOTHIRDS_WIDTH, y + HALF_HEIGHT);

			g.setColor(oColorStack.pop());
		}
		
		private double map(double iCoord, double iRouteSize, double iWindowSize) {
			
			return iCoord * iWindowSize / iRouteSize;
		}

		public void drawSDSSTP(AIM_SDSSTP oProblem, Graphics g) {

			SDSSTPSolutionInterface solution = oProblem.bestSolution;
			if (solution != null && solution.getSolutionRepresentation() != null
					&& solution.getSolutionRepresentation().getSolutionRepresentation() instanceof int[]) {

				SDSSTPSolution oSDSSTPSolution = (SDSSTPSolution) solution;
				if (solution != null && solution.getSolutionRepresentation() != null) {
					
					int[] rep = oSDSSTPSolution.getSolutionRepresentation().getSolutionRepresentation();
					SDSSTPLocation oTourOfficeLocation = oProblem.getLoadedInstance().getTourOffice();

					double max_x = Integer.MIN_VALUE;
					double max_y = Integer.MIN_VALUE;
					double min_x = Integer.MAX_VALUE;
					double min_y = Integer.MAX_VALUE;

					max_x = Math.max(max_x, oTourOfficeLocation.getX());
					max_y = Math.max(max_y, oTourOfficeLocation.getY());
					min_x = Math.min(min_x, oTourOfficeLocation.getX());
					min_y = Math.min(min_y, oTourOfficeLocation.getY());

					for (int i : rep) {

						SDSSTPLocation l = instance.getLocationForLandmark(rep[i]);
						max_x = Math.max(max_x, l.getX());
						max_y = Math.max(max_y, l.getY());
						min_x = Math.min(min_x, l.getX());
						min_y = Math.min(min_y, l.getY());
					}
					
					// calculate aspect ratio given min and max points
					Graphics2D g2 = (Graphics2D)g;
					AffineTransform initialTransform = (AffineTransform) g2.getTransform().clone();
					AffineTransform scaledTransform = g2.getTransform();
					double deltaRouteX = max_x - min_x;
					double deltaRouteY = max_y - min_y;
					double deltaWindowX = getWidth() - 20;
					double deltaWindowY = getHeight() - 20;

					// add padding and flip y axis
					scaledTransform.translate(10,getHeight()-10);
					scaledTransform.scale(1.0, -1.0);
					
					// scale to window aspect ratio
					if (deltaRouteX/deltaRouteY < deltaWindowX/deltaWindowY) {
						double widthScaling = (deltaRouteX*deltaWindowY)/(deltaRouteY*deltaWindowX);
//						scaledTransform.translate(((getWidth()-10) - (((getWidth()-20) / 2.0)) - deltaRouteX), 0);
//						scaledTransform.scale(1.0, -1.0);
						scaledTransform.scale(widthScaling, 1.0);
						System.out.println("Scale (x,y) ratio less = (" + widthScaling + "," + 1.0 + ")");
					} else {
						double widthScaling = (deltaRouteY*deltaWindowX)/(deltaRouteX*deltaWindowY);
//						scaledTransform.translate(0, 50);
//						scaledTransform.scale(1.0, -1.0);
						scaledTransform.scale(1.0, widthScaling);
//						System.out.println("Scale (x,y) ratio not less = (" + 1.0 + "," + widthScaling + ")");
					}

					g2.setTransform(scaledTransform);

					// draw tour office to first landmark
					double x1, x2, y1, y2;
					SDSSTPLocation l1 = oTourOfficeLocation, l2 = instance.getLocationForLandmark(rep[0]);
					x1 = map(l1.getX() - min_x, deltaRouteX, deltaWindowX);
					x2 = map(l2.getX() - min_x, deltaRouteX, deltaWindowX);
					y1 = map(l1.getY() - min_y, deltaRouteY, deltaWindowY);
					y2 = map(l2.getY() - min_y, deltaRouteY, deltaWindowY);

					g2.setColor(Color.YELLOW);
					new LineArrow(x1, y1, x2, y2).draw(g2);
					
					g2.setColor(oCitiesColor);
					
					// draw delivery routes
					for (int i = 0; i < rep.length - 1; i++) {

						l1 = instance.getLocationForLandmark(rep[i]);
						l2 = instance.getLocationForLandmark(rep[i + 1]);

						x1 = map(l1.getX() - min_x, deltaRouteX, deltaWindowX);
						x2 = map(l2.getX() - min_x, deltaRouteX, deltaWindowX);
						y1 = map(l1.getY() - min_y, deltaRouteY, deltaWindowY);
						y2 = map(l2.getY() - min_y, deltaRouteY, deltaWindowY);

						g2.setColor(oRoutesColor);
						new LineArrow(x1, y1, x2, y2).draw(g2);
					}

					// draw route from last landmark to office
					l1 = instance.getLocationForLandmark(rep[rep.length - 1]);
					l2 = oTourOfficeLocation;
					x1 = map(l1.getX() - min_x, deltaRouteX, deltaWindowX);
					x2 = map(l2.getX() - min_x, deltaRouteX, deltaWindowX);
					y1 = map(l1.getY() - min_y, deltaRouteY, deltaWindowY);
					y2 = map(l2.getY() - min_y, deltaRouteY, deltaWindowY);

					g2.setColor(Color.YELLOW);
					new LineArrow(x1, y1, x2, y2).draw(g2);

					drawHouse(g2, (int)x2, (int)y2, -18, -18);
					g2.setTransform(initialTransform);
					
				}
			} else {
				g.setColor(Color.WHITE);
				System.out.println("Unsupported");
				g.drawString("Unsupported solution representation...", (int) (0), (int) (getHeight() / 2.0));
			}
		}

		public void paintComponent(Graphics g) {

			super.paintComponent(g);

			int width = getWidth();
			int height = getHeight();

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);

			if (oProblem != null) {
				this.drawSDSSTP(oProblem, g);
			}

			g.dispose();

		}
	}

	@Override
	public void updateSolution(SDSSTPSolutionInterface[] current, SDSSTPSolutionInterface[] candidate,
			SDSSTPSolutionInterface best) {
		// TODO Auto-generated method stub

	}
	
    private static final Polygon ARROW_HEAD = new Polygon();

    static {
        ARROW_HEAD.addPoint(0, 0);
        ARROW_HEAD.addPoint(-5, -10);
        ARROW_HEAD.addPoint(5, -10);
    }
	
	 public static class LineArrow {

	        private final double x1;
	        private final double y1;
	        private final double x2;
	        private final double y2;

	        public LineArrow(double x, double y, double x2, double y2) {

	        	this.x1 = x;
	            this.y1 = y;
	            this.x2 = x2;
	            this.y2 = y2;
	        }

	        public void draw(Graphics g) {
	            Graphics2D g2 = (Graphics2D) g;

	            // Calcula o ângulo da seta.
	            double angle = Math.atan2(y2 - y1, x2 - x1);

	            // Desenha a linha. Corta 10 pixels na ponta para a ponta não ficar grossa.
	            g2.drawLine((int)x1, (int)y1, (int) (x2 - 10 * Math.cos(angle)), (int) (y2 - 10 * Math.sin(angle)));

	            // Obtém o AffineTransform original.
	            AffineTransform tx1 = g2.getTransform();

	            // Cria uma cópia do AffineTransform.
	            AffineTransform tx2 = (AffineTransform) tx1.clone();

	            // Translada e rotaciona o novo AffineTransform.
	            tx2.translate(x2, y2);
	            tx2.rotate(angle - Math.PI / 2);

	            // Desenha a ponta com o AffineTransform transladado e rotacionado.
	            g2.setTransform(tx2);
	            g2.fill(ARROW_HEAD);

	            // Restaura o AffineTransform original.
	            g2.setTransform(tx1);
	        }
	 }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import java.net.URL;

public class kiosk extends JFrame{
	private CardLayout cardLayout;
	private JPanel cardPanel;
    private boolean takeout = false;
    private boolean dinein = false;
    private boolean payToGf = false;
    private boolean payToCard = false;
    private boolean useCoupon = false;
    private String timestamp;
    private ShoppingCart cart; 
    private int OrderNumber = 1;
	public kiosk(){
		setTitle("kiosk");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,600);
        setLayout(new BorderLayout()); 
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        
		OpenPanel panel = new OpenPanel();
		MenuTabPanel panel2 = new MenuTabPanel();
		AutoPanel panel3 = new AutoPanel();
		cardPanel.add(panel,"Open");
		cardPanel.add(panel2,"Menu");
		cardPanel.add(panel3,"Autograph");
		
        add(cardPanel, BorderLayout.CENTER);
        cart = new ShoppingCart();
        setResizable(false);
        setLocationRelativeTo(null);
		setVisible(true);
	}
	
    class Product {
        private String name;
        private int basePrice;
        private String imagePath;
        private int productPrice;
        public Product(String name, int price, String imagePath) {
            this.name = name;
            this.basePrice = price;
            this.imagePath = imagePath;
        }
        

        public String getName() {
            return name;
        }

        public int getBasePrice() {
            return basePrice;
        }
        
        public int getProductPrice() {
            return productPrice;
        }

        public void setBurgerPrice(boolean isSet) {
            if (isSet) {
                this.productPrice = basePrice + 2;
            } else {
                this.productPrice = basePrice;
            }
        }
        
        public void setPrice(int n) {
        	this.productPrice = n;
        }
        
        public String getImagePath() {
            return imagePath;
        }
        
        
        
    }
    abstract class ProductPanel extends JPanel {
        protected BufferedImage image;
        protected Product product;
        protected JLabel nameLabel;

        public ProductPanel(Product product) {
            this.product = product;
            setLayout(new BorderLayout());
            try {
                image = ImageIO.read(new File(product.getImagePath()));
                nameLabel = new JLabel(product.getName() + " - $" + product.getBasePrice(), SwingConstants.CENTER);
                add(nameLabel, BorderLayout.SOUTH);

                addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        showDetails();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                int panelWidth = Math.max(image.getWidth()-30, getWidth());
                int panelHeight = Math.max(image.getHeight()-30, getHeight());
                setPreferredSize(new Dimension(panelWidth, panelHeight));
                revalidate();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }

        protected abstract void showDetails();
    }
    

	class OpenPanel extends JPanel{

		JButton btn_takeout = new JButton("take out");
		JButton btn_dinein = new JButton("dine-in");
	
		public OpenPanel() {
			setBackground(new Color(206, 17, 38));
			setLayout(new FlowLayout());
			
			ImageIcon logo = new ImageIcon("image/MC.png");
		        
		   
		    Image logoimage = logo.getImage();
		    Image scaledlogoImage = logoimage.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		    ImageIcon scaledlogo = new ImageIcon(scaledlogoImage);

		    JLabel label = new JLabel(scaledlogo);

		    add(label);

			btn_takeout.setPreferredSize(new Dimension(120, 200));
			btn_dinein.setPreferredSize(new Dimension(120, 200));
			add(btn_takeout);
			add(btn_dinein);
			btn_takeout.setBackground(Color.WHITE);
			btn_dinein.setBackground(Color.WHITE);
			
			btn_takeout.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
	                 takeout = true;
	                 dinein = false;
					cardLayout.show(cardPanel,"Menu");
				}
			});
			btn_dinein.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
                    takeout = false;
                    dinein = true;
					cardLayout.show(cardPanel,"Menu");
				}
			});
			
		}
	}
	
	class CartProduct{
		private String option;
		private int number;
		private int totalPrice;
		private Product product;
		public CartProduct(Product product,String option,int number) {
			this.product = product;
			this.number = number;
			this.option = option;
			this.totalPrice = product.getProductPrice() * number;
		}
		public Product getProduct() {
			return product;
		}
		public int getNumber() {
			return number;
		}
		public void setNumber(int n) {
			number = n;
			this.totalPrice = product.getProductPrice() * number;
		}
		public String getOption() {
			return option;
		}
		public int getTotalPrice() {
	        return totalPrice;
	    }
		
	}
	
	class ShoppingCart{
		private List<CartProduct> items;
		public ShoppingCart() {
			this.items = new ArrayList<>();
		}
	    public void addItem(CartProduct newItem) {
	    	boolean found = false;
	        for (CartProduct item : items) {
	            if (item.getProduct().equals(newItem.getProduct()) && item.getOption().equals(newItem.getOption())) {
	                item.setNumber(item.getNumber() + newItem.getNumber());
	                found = true;
	                break;
	            }
	        }
	        if (!found) {
	            items.add(newItem);
	        }
	    }
	    public List<CartProduct> getItems() {
	            return items;
	      	}
	    public int getTotalPrice() {
	            int totalPrice = 0;
	            for (CartProduct item : items) {
	                totalPrice += item.getTotalPrice();
	            }
	            return totalPrice;
	        }
	}
	
	
    class BurgerProductPanel extends ProductPanel {
        public BurgerProductPanel(Product product) {
            super(product);
            setBackground(Color.WHITE);
        }
        protected void showDetails() {
       
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Choose Option", true);
            dialog.setLayout(new GridLayout(3, 1));

            JLabel nameLabel = new JLabel(product.getName());
            dialog.add(nameLabel);

            JRadioButton setRadioButton = new JRadioButton("Set(+2$)");
            JRadioButton singleItemRadioButton = new JRadioButton("Single Item");

            ButtonGroup radioButtonGroup = new ButtonGroup();
            radioButtonGroup.add(setRadioButton);
            radioButtonGroup.add(singleItemRadioButton);

            JPanel radioPanel = new JPanel(new GridLayout(1, 2));
            radioPanel.add(setRadioButton);
            radioPanel.add(singleItemRadioButton);

            dialog.add(radioPanel);
           

            JButton confirmButton = new JButton("Confirm");
            confirmButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean isSet = setRadioButton.isSelected();
                    boolean isSingleItem = singleItemRadioButton.isSelected();
                    
                    if (isSet || isSingleItem) {
                        
                        product.setBurgerPrice(isSet);
                        cart.addItem(new CartProduct(product, isSet ? "set" : "single", 1));
                    }
                    dialog.dispose();
                    JOptionPane.showMessageDialog(null, "product added", "check", JOptionPane.PLAIN_MESSAGE);
                }
                

            });

            dialog.add(confirmButton);

            dialog.setSize(300, 200);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }
        }
    
    class SideProductPanel extends ProductPanel {
        public SideProductPanel(Product product) {
            super(product);
            setBackground(Color.WHITE);
        }
        protected void showDetails() {
            product.setPrice(product.getBasePrice());
        	 cart.addItem(new CartProduct(product,"side",1));
        	 JOptionPane.showMessageDialog(null, "product added", "check", JOptionPane.PLAIN_MESSAGE);
            
            
        }
    }
    
    class DrinkProductPanel extends ProductPanel {
        public DrinkProductPanel(Product product) {
            super(product);
            setBackground(Color.WHITE);
        }
        protected void showDetails() {
        	 product.setPrice(product.getBasePrice());
        	 cart.addItem(new CartProduct(product,"drink",1));
        	 JOptionPane.showMessageDialog(null, "product added", "check", JOptionPane.PLAIN_MESSAGE);
        }
    }

	
	class BugerPanel extends JPanel{
        public BugerPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            setBackground(Color.WHITE);

            add(new BurgerProductPanel(new Product("BigMac", 5, "image/bigmac.png")));
            add(new BurgerProductPanel(new Product("Shanghi", 6, "image/shanghi.png")));
            add(new BurgerProductPanel(new Product("1955 Buger", 7, "image/1955Buger.png")));
            add(new BurgerProductPanel(new Product("SubiBuger", 8, "image/SubiBuger.png")));
        }
	}
	
	class sidePanel extends JPanel{
		public sidePanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            setBackground(Color.WHITE);

            add(new SideProductPanel(new Product("French Fries", 3, "image/FF.png")));
            add(new SideProductPanel(new Product("Hash Brown", 2, "image/hashbrown.png")));
            add(new SideProductPanel(new Product("McNuggets", 4, "image/McNuggets.png")));
        }
	}
	class drinkPanel extends JPanel{
	       public drinkPanel() {
	            setLayout(new FlowLayout(FlowLayout.CENTER));
	            setBackground(Color.WHITE);

	            add(new DrinkProductPanel(new Product("IceAmericano", 3, "image/AA.png")));
	            add(new DrinkProductPanel(new Product("Iced latte", 4, "image/Icedlatte.png")));
	            add(new DrinkProductPanel(new Product("ZeroCoke", 2, "image/ZeroCoke.png")));
	            add(new DrinkProductPanel(new Product("Coke", 2, "image/Coke.png")));
	            add(new DrinkProductPanel(new Product("Sprite", 2, "image/Sprite.png")));
	        }
	}
	
    class MenuTabPanel extends JPanel {
        private JTabbedPane tabbedPane;

        public MenuTabPanel() {
            setLayout(new BorderLayout());
            tabbedPane = new JTabbedPane();
            tabbedPane.setTabPlacement(JTabbedPane.LEFT);
            BugerPanel burgerPanel = new BugerPanel();
            sidePanel sidePanel = new sidePanel();
            drinkPanel drinkPanel = new drinkPanel();

            tabbedPane.addTab("Burgers", burgerPanel);
            tabbedPane.addTab("Sides", sidePanel);
            tabbedPane.addTab("Drinks",drinkPanel);

            add(tabbedPane, BorderLayout.CENTER);
            
            JPanel btnPanel = new JPanel();
            JButton cartbtn = new JButton("Cart");
            cartbtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
            		CartPanel panel3 = new CartPanel();
            		cardPanel.add(panel3,"Cart");
                	cardLayout.show(cardPanel,"Cart");
                }
            });
            btnPanel.add(cartbtn);

            add(btnPanel, BorderLayout.SOUTH);
        }
    }
  
    class CartTitle extends JPanel{
    
    	public CartTitle() {
    		setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            JLabel CartPage = new JLabel("Cart");
            CartPage.setFont(new Font("Arial",Font.BOLD,30));
            JButton goMenu = new JButton("go back");
            goMenu.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		cardLayout.show(cardPanel,"Menu");
            	}
            });
            
            
            add(CartPage,BorderLayout.NORTH);
            add(goMenu,BorderLayout.CENTER);
    	}
    	
       
    }
    
    
    
   class CartPanel extends JPanel {
        private List<CartProduct> cartItems = cart.getItems();

        public CartPanel() {
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());

            add(new CartTitle(), BorderLayout.NORTH);

            JPanel productPanel = new JPanel();
            productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

            for (CartProduct item : cartItems) {
                JPanel cartProductPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                cartProductPanel.setBackground(Color.WHITE);

                Product product = item.getProduct();
                int totalPrice = item.getTotalPrice();
                String option = item.getOption();
                int[] quantityWrapper = {item.getNumber()};

                JLabel cartProductLabel = new JLabel(product.getName() + " " + option + " x" + quantityWrapper[0]
                        + " " + String.valueOf(totalPrice) + "$");

                JButton addButton = new JButton("+");
                JButton minusButton = new JButton("-");
                addButton.setPreferredSize(new Dimension(50, 30));
                addButton.setMargin(new Insets(5, 5, 5, 5));
                minusButton.setPreferredSize(new Dimension(50, 30));
                minusButton.setMargin(new Insets(5, 5, 5, 5));

                addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        quantityWrapper[0]++;
                        item.setNumber(quantityWrapper[0]);
                        int totalPrice = item.getTotalPrice();
                        updateCountLabel(cartProductLabel, product, option, quantityWrapper[0], totalPrice);
                        updateTotalLabel(productPanel);
                    }
                });

                minusButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (quantityWrapper[0] != 0) {
                            quantityWrapper[0]--;
                            item.setNumber(quantityWrapper[0]);
                            int totalPrice = item.getTotalPrice();
                            updateCountLabel(cartProductLabel, product, option, quantityWrapper[0], totalPrice);
                            updateTotalLabel(productPanel);
                        }
                    }
                });

                cartProductPanel.add(cartProductLabel);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
                buttonPanel.add(addButton);
                buttonPanel.add(minusButton);
                cartProductPanel.add(buttonPanel);

                productPanel.add(cartProductPanel);
            }

           
            add(productPanel, BorderLayout.CENTER);

            JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            totalPanel.setBackground(Color.WHITE);
            JLabel totalLabel = new JLabel("Total Price: " + String.valueOf(updateTotalPrice(cartItems)) + "$");
            totalLabel.setFont(new Font("Arial", Font.BOLD, 25));
            totalPanel.add(totalLabel);
            JButton Payforbtn = new JButton("Pay for");
            totalPanel.add(Payforbtn);
            Payforbtn.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		PayPanel panel3 = new PayPanel(cart.getItems());
            		cardPanel.add(panel3,"Pay");
            		cardLayout.show(cardPanel,"Pay");
            	}
            });
            
            
            add(totalPanel, BorderLayout.SOUTH);
        }

        private void updateCountLabel(JLabel cartProductLabel, Product product, String option, int quantity, int totalPrice) {
            cartProductLabel.setText(product.getName() + " " + option + " x" + String.valueOf(quantity)
                    + " " + String.valueOf(totalPrice) + "$");
        }

        private int updateTotalPrice(List<CartProduct> cartItems) {
            int totalPrice = 0;
            for (CartProduct item : cartItems) {
                totalPrice += item.getTotalPrice();
            }
            return totalPrice;
        }

        private void updateTotalLabel(JPanel productPanel) {
            Component[] components = productPanel.getComponents();
            int totalPrice = 0;

            for (Component component : components) {
                if (component instanceof JPanel) {
                    JPanel cartProductPanel = (JPanel) component;
                    JLabel cartProductLabel = (JLabel) cartProductPanel.getComponent(0);
                    String labelText = cartProductLabel.getText();
                    String[] parts = labelText.split(" ");
                    totalPrice += Integer.parseInt(parts[parts.length - 1].replace("$", ""));
                }
            }

            Component[] totalPanelComponents = ((JPanel) getComponent(2)).getComponents();
            JLabel totalLabel = (JLabel) totalPanelComponents[0];
            totalLabel.setText("Total Price: " + String.valueOf(totalPrice) + "$");
        }
    }
    
   
   class PayPanel extends JPanel {
	
	    private List<CartProduct> cartItems;
	    private JTextArea orderHistoryTextArea;
	    private JLabel totalLabel;
	    private StringBuilder orderHistory;
	    public PayPanel(List<CartProduct> cartItems) {
	        this.cartItems = cartItems;
	        setBackground(Color.WHITE);

	        setLayout(new BorderLayout());
	        JPanel PayTopPanel = new JPanel();
	        
	        JPanel paymentPanel = new JPanel(new FlowLayout());
	        JButton SelectPayment = new JButton("Select Payment");
	        
	        paymentPanel.add(SelectPayment);
	        PayTopPanel.add(paymentPanel);
	        
	        JPanel couponPanel = new JPanel(new FlowLayout());
	        JButton couponButton = new JButton("Use Coupon to discount");
	        
	        
	        couponPanel.add(couponButton);
	        PayTopPanel.add(couponPanel);
	    
	        orderHistoryTextArea = new JTextArea();
	        orderHistoryTextArea.setEditable(false);

	        JScrollPane scrollPane = new JScrollPane(orderHistoryTextArea);
	        
	        totalLabel = new JLabel("Total Price: " + String.valueOf(updateTotalPrice(cartItems)) + "$");
	        JPanel PatBottomPanel = new JPanel();
	        JButton PayConfirmbtn = new JButton("Confirm");
	        
	        PatBottomPanel.add(totalLabel);
	        PatBottomPanel.add(PayConfirmbtn);
	        
	        add(PayTopPanel,BorderLayout.NORTH);
	        add(scrollPane, BorderLayout.CENTER);
	        add(PatBottomPanel,BorderLayout.SOUTH);
	        
	        SelectPayment.addActionListener(e -> SelectPaymentAction());
	        couponButton.addActionListener(e -> useCouponAction());
	        PayConfirmbtn.addActionListener(e -> PayConfirmbtnAction());
	        updateOrderHistory();
	    }
	    private void updateOrderHistory() {
	        orderHistory = new StringBuilder("Order History:\n");

	        for (CartProduct item : cartItems) {
	            Product product = item.getProduct();
	            String option = item.getOption();
	            int quantity = item.getNumber();
	            int totalPrice = item.getTotalPrice();

	            orderHistory.append(product.getName())
	                    .append(" - ")
	                    .append(option)
	                    .append(" x")
	                    .append(quantity)
	                    .append(" - $")
	                    .append(totalPrice)
	                    .append("\n");
	        }

	        orderHistoryTextArea.setText(orderHistory.toString());
	    }
	    private int updateTotalPrice(List<CartProduct> cartItems) {
	        int totalPrice = 0;
	        for (CartProduct item : cartItems) {
	            totalPrice += item.getTotalPrice();
	        }
	        
	        if (useCoupon) {
	            double discountedPrice = totalPrice * 0.9;
	            totalPrice = (int) Math.round(discountedPrice);
	        }

	        return totalPrice;
	    }
	    
	    private void SelectPaymentAction() {
	    	JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Select Payment", true);
	    	dialog.setLayout(new GridLayout(2, 1));
	    	JRadioButton CardRadioButton = new JRadioButton("Pay to card");
	    	JRadioButton GFRadioButton = new JRadioButton("Use Gifticon");
	    	
	    	JPanel RadioPanel = new JPanel();
	    	RadioPanel.add(CardRadioButton);
	    	RadioPanel.add(GFRadioButton);
	    	
	    	ButtonGroup radioButtonGroup = new ButtonGroup();
	        radioButtonGroup.add(CardRadioButton);
	        radioButtonGroup.add(GFRadioButton);
	    	JButton confirmButton = new JButton("Confirm");
            confirmButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	 boolean isCard = CardRadioButton.isSelected();
                	 boolean isGf = GFRadioButton.isSelected();
                	 if (isCard) {
                         payToCard = true;
                         payToGf = false;
                     }else if(isGf) {
                    	 payToCard = false;
                    	 payToGf = true;
                     }
                     dialog.dispose();
                     JOptionPane.showMessageDialog(null, "Payment Selected", "check", JOptionPane.PLAIN_MESSAGE);
                }
            });
	    	
	    	dialog.add(RadioPanel);
	    	dialog.add(confirmButton);

            dialog.setSize(300, 200);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
	    }

	    private void useCouponAction() {
	        String userInput = JOptionPane.showInputDialog(PayPanel.this, "Input Your coupon code");
	        if ("CSE2107".equals(userInput)) {
	            useCoupon = true;
	            updateTotalAndDisplay();
	        }
	    }
	    private void updateTotalAndDisplay() {
	        int totalPrice = updateTotalPrice(cartItems);
	        totalLabel.setText("Total Price: " + String.valueOf(totalPrice) + "$");
	        orderHistory.append("Coupon applied (-10%)");
	        orderHistoryTextArea.setText(orderHistory.toString());
	    }
	    
	    private void PayConfirmbtnAction() {
	    	if((payToCard == false)&&(payToGf == false)) {
	    		JOptionPane.showMessageDialog(null, "Please Select Payment", "!", JOptionPane.ERROR_MESSAGE);
	    	}else {
	    		
	    		cardLayout.show(cardPanel,"Autograph");
	    	}
	    }
   }
	    
   class DrawingPanel extends JPanel {
	    private List<Point> points;
	    private String timestamp;
	    public DrawingPanel() {
	        points = new ArrayList<>();

	        addMouseMotionListener(new MouseAdapter() {
	            public void mouseDragged(MouseEvent e) {
	                points.add(e.getPoint());
	                repaint();
	            }
	        });
	        
	    }

        public String getTimestamp() {
            return timestamp;
        }

        protected void setTimestamp() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timestamp = dateFormat.format(new Date());
        }
	    public void clearDrawing() {
	        points.clear();
	        repaint();
	    }
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);

	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setColor(Color.BLACK);
	        g2d.setStroke(new BasicStroke(2));

	        for (int i = 0; i < points.size() - 1; i++) {
	            Point startPoint = points.get(i);
	            Point endPoint = points.get(i + 1);
	            g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
	        }
	    }
	    public List<Point> getDrawingPoints() {
	        return points;
	    }
	    
	    public BufferedImage getDrawingImage() {
	        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2d = image.createGraphics();
	        paint(g2d);
	        g2d.dispose();
	        return image;
	    }
	    
	}
   
   
   class ReceiptPanel extends JPanel {
	    private BufferedImage drawingImage;
	    private List<CartProduct> cartItems;
	    private JTextArea orderHistoryTextArea;

	    private StringBuilder orderHistory;

	    public ReceiptPanel(BufferedImage drawingImage, List<CartProduct> cartItems) {
	        this.drawingImage = drawingImage;
	        this.cartItems = cartItems;
	        setBackground(Color.WHITE);

	        setLayout(new BorderLayout());

	        JPanel orderHistoryPanel = new JPanel();
	        orderHistoryPanel.setLayout(new BoxLayout(orderHistoryPanel, BoxLayout.Y_AXIS));
	        orderHistoryTextArea = new JTextArea();
	        orderHistoryTextArea.setEditable(false);

	        JScrollPane scrollPane = new JScrollPane(orderHistoryTextArea);
	        orderHistoryPanel.add(scrollPane);

	        JPanel drawingPanel = new JPanel() {
	            protected void paintComponent(Graphics g) {
	                super.paintComponent(g);
	                g.drawImage(drawingImage, 0, 0, this);
	            }
	        };
	        drawingPanel.setPreferredSize(new Dimension(400, 300)); 

	        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	        JButton ENDButton = new JButton("Confirm");
	        bottomPanel.add(ENDButton);

	        ENDButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                cardLayout.show(cardPanel, "Open");
	                cartItems.clear();
	                takeout = false;
	                dinein = false;
	                payToGf = false;
	                payToCard = false;
	                useCoupon = false;
	                OrderNumber++;
	            }
	        });

	        updateOrderHistory();
	        add(orderHistoryPanel, BorderLayout.NORTH);
	        add(drawingPanel, BorderLayout.CENTER);
	        add(bottomPanel, BorderLayout.SOUTH);
	        
	    }

	    private void updateOrderHistory() {
	        orderHistory = new StringBuilder("RECEIPT: ");
	        orderHistory.append(OrderNumber+"\n");
	        if(dinein) {
	        	orderHistory.append("Dine in"+"\n");
	        }else {
	        	orderHistory.append("Take out"+"\n");
	        }
	        if(payToCard) {
	        	orderHistory.append("Payment : Card"+"\n");
	        }else {
	        	orderHistory.append("Payment : GIFTICON"+"\n");
	        }
	      
	        for (CartProduct item : cartItems) {
	            Product product = item.getProduct();
	            String option = item.getOption();
	            int quantity = item.getNumber();
	            int totalPrice = item.getTotalPrice();
	            if(quantity == 0) {
	            	continue;
	            }
	            orderHistory.append(product.getName())
	                    .append(" - ")
	                    .append(option)
	                    .append(" x")
	                    .append(quantity)
	                    .append(" - $")
	                    .append(totalPrice)
	                    .append("\n");
	        }
	        if(useCoupon) {
	        	orderHistory.append("Coupon applied (-10%)")
	        				.append("\n");
	        }
	        orderHistory.append("Total Price: " + String.valueOf(updateTotalPrice(cartItems)) + "$");
	        orderHistoryTextArea.setText(orderHistory.toString());
	    }
	    private int updateTotalPrice(List<CartProduct> cartItems) {
	        int totalPrice = 0;
	        for (CartProduct item : cartItems) {
	            totalPrice += item.getTotalPrice();
	        }
	        
	        if (useCoupon) {
	            double discountedPrice = totalPrice * 0.9;
	            totalPrice = (int) Math.round(discountedPrice);
	        }

	        return totalPrice;
	    }
	    
	}

	class AutoPanel extends JPanel {
	    private DrawingPanel drawingPanel;

	    public AutoPanel() {
	    	
	        setLayout(new BorderLayout());
	        drawingPanel = new DrawingPanel();
	        drawingPanel.setBackground(Color.WHITE);
	        JLabel autoGraphLabel = new JLabel("Auto Graph please");
	        JButton autoConfirmButton = new JButton("Confirm");

	        setPreferredSize(new Dimension(400, 400));
	        drawingPanel.setPreferredSize(new Dimension(400, 300));

	        add(drawingPanel, BorderLayout.CENTER);
	        add(autoConfirmButton, BorderLayout.SOUTH);
	        add(autoGraphLabel, BorderLayout.NORTH);

	        autoConfirmButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	drawingPanel.setTimestamp();
	            	BufferedImage drawingImage = drawingPanel.getDrawingImage();
	            	ReceiptPanel confirmPanel = new ReceiptPanel(drawingImage,cart.getItems());

	                JPanel parentPanel = (JPanel) getParent();
	                parentPanel.add(confirmPanel, "ReceiptPanel");

	                CardLayout cardLayout = (CardLayout) parentPanel.getLayout();
	                cardLayout.show(parentPanel, "ReceiptPanel");
	                drawingPanel.clearDrawing();
	            }
	        });
	        drawingPanel.setTimestamp();
	    }
	    
	}
	 
    
	public static void main(String[] args) {
		new kiosk();
		
	}

	
}

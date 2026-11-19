import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Comparator;

public class NderfaqjaGrafike extends JFrame {
    private SistemiRezervimeve sistemi;
    private CardLayout cardLayout;
    private JPanel qendraKardave;

    // Kontrollet e kërkimit në faqen Home
    private JComboBox<String> filtroNisja;
    private JComboBox<String> filtroMberritja;
    private JComboBox<String> filtroData;
    private JComboBox<String> filtroKlasa;
    private JTextField fushaPromoCode;
    private JRadioButton rbOneWay;
    private JRadioButton rbReturn;
    private JCheckBox chkVetemTeLirat;

    // Panelët e kartelave dhe navigimit
    private JPanel listaKartelaveFluturimeve;
    private JLabel lblTitulliRezultateve;
    private JPanel listaKartelaveBiletave;
    private JPanel paneliKonfirmimit;
    private String nisjaFundit = "Të gjitha";
    private String mberritjaFundit = "Të gjitha";
    private ArrayList<JButton> navButonat = new ArrayList<>();

    // Paleta Zyrtare: BURGUNDY, PINK, BLUSH PINK, WHITE & GOLD
    private final Color BURGUNDY = new Color(92, 12, 48);           // #5C0C30
    private final Color PINK_LUX = new Color(188, 36, 108);         // #BC246C
    private final Color PINK_SOFT = new Color(242, 130, 178);       // #F282B2
    private final Color BLUSH_PINK = new Color(255, 238, 245);      // #FFEEF5
    private final Color BLUSH_BG = new Color(253, 247, 250);        // #FDF7FA
    private final Color CHAMPAGNE_GOLD = new Color(235, 185, 92);   // #EBB95C
    private final Color E_KUQE_ZENE = new Color(210, 45, 70);

    public NderfaqjaGrafike() {
        sistemi = new SistemiRezervimeve();

        setTitle("COPA AIR  |  Pink Ombré Luxury Aviation — 33 Destinations, 5★ Hotels & Pink VIP");
        setSize(1240, 810);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BLUSH_BG);

        // ================= 1. TOP LUXURY NAVIGATION BAR =================
        JPanel navBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, BURGUNDY, getWidth(), 0, PINK_LUX));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        navBar.setBorder(new EmptyBorder(14, 34, 14, 34));

        JButton logoCopa = new JButton("<html><font color='#EBB95C' size='5'><b>✦ COPA</b></font> <font color='white' size='5'><b>AIR</b></font></html>");
        logoCopa.setBorderPainted(false);
        logoCopa.setContentAreaFilled(false);
        logoCopa.setFocusPainted(false);
        logoCopa.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel navCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 2));
        navCenter.setOpaque(false);

        JButton btnNavHome = krijoLinkNav("Home");
        JButton btnNavDestinations = krijoLinkNav("Destinations (33)");
        JButton btnNavFlights = krijoLinkNav("Flights & Booking");
        JButton btnNavHotels = krijoLinkNav("Luxury 5★ Hotels");
        JButton btnNavVip = krijoLinkNav("Pink VIP");
        JButton btnNavCheckIn = krijoLinkNav("Online Check-in");
        JButton btnNavMyTrips = krijoLinkNav("My Trips");

        navCenter.add(btnNavHome);
        navCenter.add(btnNavDestinations);
        navCenter.add(btnNavFlights);
        navCenter.add(btnNavHotels);
        navCenter.add(btnNavVip);
        navCenter.add(btnNavCheckIn);
        navCenter.add(btnNavMyTrips);

        JButton btnNavAdmin = new RoundedButton("⚙ Admin", new Color(125, 20, 68), CHAMPAGNE_GOLD, 20);
        btnNavAdmin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNavAdmin.setPreferredSize(new Dimension(105, 34));

        navBar.add(logoCopa, BorderLayout.WEST);
        navBar.add(navCenter, BorderLayout.CENTER);
        navBar.add(btnNavAdmin, BorderLayout.EAST);
        add(navBar, BorderLayout.NORTH);

        // ================= 2. FAQET E NDARA ME CARDLAYOUT =================
        cardLayout = new CardLayout();
        qendraKardave = new JPanel(cardLayout);

        qendraKardave.add(krijoFaqenHome(), "HOME");
        qendraKardave.add(krijoFaqenDestinacioneve(), "DESTINATIONS");
        qendraKardave.add(krijoFaqenFluturimeve(), "FLIGHTS");
        qendraKardave.add(krijoFaqenHoteleve(), "HOTELS");
        qendraKardave.add(krijoFaqenPinkVip(), "VIP");
        qendraKardave.add(krijoFaqenOnlineCheckIn(), "CHECKIN");
        qendraKardave.add(krijoFaqenMyTrips(), "MY_TRIPS");
        qendraKardave.add(krijoFaqenKonfirmimit(), "CONFIRMATION");
        qendraKardave.add(krijoFaqenAdmin(), "ADMIN");

        add(qendraKardave, BorderLayout.CENTER);

        // ================= 3. FOOTER PROFESIONAL =================
        add(krijoFooterinProfesional(), BorderLayout.SOUTH);

        // Lidhja e Menusë
        logoCopa.addActionListener(e -> shkoTeFaqja("HOME", btnNavHome));
        btnNavHome.addActionListener(e -> shkoTeFaqja("HOME", btnNavHome));
        btnNavDestinations.addActionListener(e -> shkoTeFaqja("DESTINATIONS", btnNavDestinations));
        btnNavFlights.addActionListener(e -> {
            shfaqKartelatEFluturimeve("Të gjitha", "Të gjitha", false);
            shkoTeFaqja("FLIGHTS", btnNavFlights);
        });
        btnNavHotels.addActionListener(e -> shkoTeFaqja("HOTELS", btnNavHotels));
        btnNavVip.addActionListener(e -> shkoTeFaqja("VIP", btnNavVip));
        btnNavCheckIn.addActionListener(e -> shkoTeFaqja("CHECKIN", btnNavCheckIn));
        btnNavMyTrips.addActionListener(e -> {
            rifreskoMyTrips();
            shkoTeFaqja("MY_TRIPS", btnNavMyTrips);
        });
        btnNavAdmin.addActionListener(e -> shkoTeFaqja("ADMIN", null));

        shkoTeFaqja("HOME", btnNavHome);
    }

    private void shkoTeFaqja(String emriFaqes, JButton butoniAktiv) {
        for (JButton b : navButonat) {
            b.setForeground(new Color(255, 228, 240));
            b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }
        if (butoniAktiv != null) {
            butoniAktiv.setForeground(CHAMPAGNE_GOLD);
            butoniAktiv.setFont(new Font("Segoe UI", Font.BOLD, 14));
        }
        cardLayout.show(qendraKardave, emriFaqes);
    }

    // ================= FAQJA 1: HOME (DIZAJN I HAPUR ME PAMJE PANORAMIKE) =================
    private JPanel krijoFaqenHome() {
        JPanel homePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Sfondi kryesor Pink Ombré Luxury (Burgundy -> Pink -> Blush Pink)
                GradientPaint sky = new GradientPaint(0, 0, BURGUNDY, w, h, new Color(247, 175, 208));
                g2.setPaint(sky);
                g2.fillRect(0, 0, w, h);

                // Dielli i artë në horizont & drita rrezatuese
                RadialGradientPaint sunGlow = new RadialGradientPaint(
                    new Point((int)(w * 0.78), (int)(h * 0.28)), 340,
                    new float[]{0.0f, 0.5f, 1.0f},
                    new Color[]{new Color(255, 230, 170, 110), new Color(242, 130, 178, 45), new Color(255, 255, 255, 0)}
                );
                g2.setPaint(sunGlow);
                g2.fillOval((int)(w * 0.78) - 340, (int)(h * 0.28) - 340, 680, 680);

                // Re të buta transparente
                g2.setColor(new Color(255, 255, 255, 28));
                g2.fillRoundRect(w - 420, 95, 310, 52, 50, 50);
                g2.fillRoundRect(w - 340, 125, 270, 44, 44, 44);
                g2.fillRoundRect(80, h - 180, 360, 55, 55, 55);

                // Harku i artë i fluturimit dhe silueta elegante e avionit
                g2.setColor(new Color(235, 185, 92, 165));
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{8, 8}, 0));
                g2.drawArc(w - 640, 60, 580, 260, 20, 150);

                // Silueta e horizontit në fund
                g2.setColor(new Color(92, 12, 48, 55));
                g2.fillArc(-100, h - 110, w + 200, 220, 0, 180);
            }
        };
        homePanel.setBorder(new EmptyBorder(48, 70, 40, 70));

        // Hero Section i hapur (pa kuti rreth tekstit!)
        JPanel heroSection = new JPanel();
        heroSection.setOpaque(false);
        heroSection.setLayout(new BoxLayout(heroSection, BoxLayout.Y_AXIS));

        JLabel kicker = new JLabel("✦  ELEVATE YOUR JOURNEY  •  PINK OMBRÉ LUXURY EDITION");
        kicker.setForeground(CHAMPAGNE_GOLD);
        kicker.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel titulli = new JLabel("<html>Fly Beyond Ordinary.<br>Arrive in Pure Luxury.</html>");
        titulli.setFont(new Font("Segoe UI", Font.BOLD, 44));
        titulli.setForeground(Color.WHITE);

        JLabel nentitulli = new JLabel("Eksploroni 33 destinacione ekskluzive, kabinën tonë të re dhe hotele me 5 yje.");
        nentitulli.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        nentitulli.setForeground(BLUSH_PINK);

        heroSection.add(kicker);
        heroSection.add(Box.createVerticalStrut(12));
        heroSection.add(titulli);
        heroSection.add(Box.createVerticalStrut(12));
        heroSection.add(nentitulli);

        homePanel.add(heroSection, BorderLayout.NORTH);

        // Shiriti i Kërkimit (Glassmorphic Open Bar - pa e ndarë çdo fushë në kuti të trasha)
        JPanel centerArea = new JPanel(new GridBagLayout());
        centerArea.setOpaque(false);

        RoundedPanel glassSearch = new RoundedPanel(34, new Color(255, 255, 255, 238), CHAMPAGNE_GOLD);
        glassSearch.setLayout(new BorderLayout(16, 14));
        glassSearch.setPreferredSize(new Dimension(1060, 195));
        glassSearch.setBorder(new EmptyBorder(18, 28, 18, 28));

        JPanel topControls = new JPanel(new BorderLayout());
        topControls.setOpaque(false);

        JPanel leftOpts = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        leftOpts.setOpaque(false);
        rbOneWay = new JRadioButton("One-way", true);
        rbReturn = new JRadioButton("Round-trip");
        chkVetemTeLirat = new JCheckBox("💸 Cheapest Fares First");
        rbOneWay.setOpaque(false); rbReturn.setOpaque(false); chkVetemTeLirat.setOpaque(false);
        rbOneWay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        rbReturn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        chkVetemTeLirat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        rbOneWay.setForeground(PINK_LUX);
        chkVetemTeLirat.setForeground(BURGUNDY);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbOneWay); bg.add(rbReturn);
        leftOpts.add(rbOneWay); leftOpts.add(rbReturn); leftOpts.add(chkVetemTeLirat);

        JPanel rightPromo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPromo.setOpaque(false);
        JLabel lPromo = new JLabel("🏷 Promo (COPA20):");
        lPromo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lPromo.setForeground(BURGUNDY);
        fushaPromoCode = new JTextField(8);
        rightPromo.add(lPromo); rightPromo.add(fushaPromoCode);

        topControls.add(leftOpts, BorderLayout.WEST);
        topControls.add(rightPromo, BorderLayout.EAST);

        // Fushat e hapura të kërkimit me ndarës të hollë (jo kuti!)
        JPanel fieldsRow = new JPanel(new GridLayout(1, 4, 18, 0));
        fieldsRow.setOpaque(false);

        filtroNisja = new JComboBox<>();
        filtroMberritja = new JComboBox<>();
        filtroNisja.addItem("All Origins (Nga)");
        filtroMberritja.addItem("All Destinations (Në)");
        for (Aeroporti a : sistemi.getAeroportet()) {
            filtroNisja.addItem(a.getKodiIATA() + " - " + a.getQyteti());
            filtroMberritja.addItem(a.getKodiIATA() + " - " + a.getQyteti());
        }

        filtroData = new JComboBox<>(new String[]{
            "Dhjetor 2026 (Të gjitha)", "2026-12-15", "2026-12-16", "2026-12-18", "2026-12-20"
        });
        filtroKlasa = new JComboBox<>(new String[]{
            "Economy & Business", "Economy Class", "Pink Luxury Business"
        });

        fieldsRow.add(krijoFusheTeHapur("FROM / ORIGIN", filtroNisja));
        fieldsRow.add(krijoFusheTeHapur("TO / DESTINATION", filtroMberritja));
        fieldsRow.add(krijoFusheTeHapur("DEPARTURE DATE", filtroData));
        fieldsRow.add(krijoFusheTeHapur("CABIN CLASS", filtroKlasa));

        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setOpaque(false);

        JButton btnExploreDest = new JButton("🌍 Shiko Galerinë me 33 Destinacione  →");
        btnExploreDest.setBorderPainted(false);
        btnExploreDest.setContentAreaFilled(false);
        btnExploreDest.setForeground(BURGUNDY);
        btnExploreDest.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExploreDest.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExploreDest.addActionListener(e -> shkoTeFaqja("DESTINATIONS", navButonat.get(1)));

        JButton btnSearch = new RoundedButton("✈  Search Flights", PINK_LUX, Color.WHITE, 26);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSearch.setPreferredSize(new Dimension(230, 44));
        btnSearch.addActionListener(e -> {
            String n = filtroNisja.getSelectedIndex() == 0 ? "Të gjitha" : (String) filtroNisja.getSelectedItem();
            String m = filtroMberritja.getSelectedIndex() == 0 ? "Të gjitha" : (String) filtroMberritja.getSelectedItem();
            shfaqKartelatEFluturimeve(n, m, chkVetemTeLirat.isSelected());
            shkoTeFaqja("FLIGHTS", navButonat.get(2));
        });

        bottomBar.add(btnExploreDest, BorderLayout.WEST);
        bottomBar.add(btnSearch, BorderLayout.EAST);

        glassSearch.add(topControls, BorderLayout.NORTH);
        glassSearch.add(fieldsRow, BorderLayout.CENTER);
        glassSearch.add(bottomBar, BorderLayout.SOUTH);

        centerArea.add(glassSearch);
        homePanel.add(centerArea, BorderLayout.CENTER);

        // Quick Highlights të hapura poshtë (pa kuti të mbyllura)
        JPanel quickStrip = new JPanel(new GridLayout(1, 3, 30, 0));
        quickStrip.setOpaque(false);
        quickStrip.add(krijoHighlightTeHapur("🌍 33 Global Destinations", "Nga Prishtina & Tirana drejt Parisit, Dubait, Tokios dhe Nju Jorkut."));
        quickStrip.add(krijoHighlightTeHapur("🏨 5★ Luxury Partner Hotels", "Zgjidhni hotele me deri në 5 yje direkt gjatë rezervimit."));
        quickStrip.add(krijoHighlightTeHapur("👑 Pink VIP Membership", "Silver, Gold & Diamond me zbritje ekskluzive deri në 20%."));
        homePanel.add(quickStrip, BorderLayout.SOUTH);

        return homePanel;
    }

    private JPanel krijoFusheTeHapur(String etiketa, JComboBox<String> combo) {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PINK_SOFT));
        JLabel l = new JLabel(etiketa);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(PINK_LUX);
        combo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        combo.setBackground(Color.WHITE);
        p.add(l, BorderLayout.NORTH);
        p.add(combo, BorderLayout.CENTER);
        return p;
    }

    private JPanel krijoHighlightTeHapur(String titulli, String teksti) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(titulli);
        t.setForeground(CHAMPAGNE_GOLD);
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JLabel d = new JLabel("<html><font color='#FFEEF5'>" + teksti + "</font></html>");
        d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        p.add(t);
        p.add(Box.createVerticalStrut(4));
        p.add(d);
        return p;
    }

    // ================= FAQJA 2: GALERIA VIZUALE ME 33 DESTINACIONE =================
    private JPanel krijoFaqenDestinacioneve() {
        JPanel page = new JPanel(new BorderLayout(15, 15));
        page.setBackground(BLUSH_BG);
        page.setBorder(new EmptyBorder(24, 45, 24, 45));

        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);
        JLabel tit = new JLabel("<html><font color='#5C0C30' size='5'><b>🌍 Eksploroni 33 Destinacionet Tona Botërore</b></font><br>"
                + "<font color='#BC246C'>Klikoni mbi cilindo qytet për të parë fluturimet dhe oraret e disponueshme</font></html>");
        head.add(tit, BorderLayout.WEST);

        JPanel grid = new JPanel(new GridLayout(0, 3, 22, 22));
        grid.setBackground(BLUSH_BG);

        ArrayList<Aeroporti> aeroportet = sistemi.getAeroportet();
        for (int i = 0; i < aeroportet.size(); i++) {
            Aeroporti a = aeroportet.get(i);
            double cmimiFillestar = sistemi.merrCmiminFillestarPerQytet(a.getKodiIATA());
            final int seed = i;

            RoundedPanel card = new RoundedPanel(24, Color.WHITE, new Color(240, 200, 220));
            card.setLayout(new BorderLayout());
            card.setPreferredSize(new Dimension(340, 225));
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Pamje artistike panoramike e qytetit (gjeneruar grafikisht me nuanca luksoze)
            JPanel cityBanner = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int w = getWidth(), h = getHeight();

                    Color c1 = (seed % 3 == 0) ? BURGUNDY : (seed % 3 == 1) ? PINK_LUX : new Color(138, 24, 78);
                    Color c2 = (seed % 2 == 0) ? PINK_SOFT : CHAMPAGNE_GOLD;
                    g2.setPaint(new GradientPaint(0, 0, c1, w, h, c2));
                    g2.fillRoundRect(0, 0, w, h + 15, 24, 24);

                    // Dielli/Hëna dhe silueta e ndërtesave të qytetit
                    g2.setColor(new Color(255, 255, 255, 65));
                    g2.fillOval(w - 75, 16, 44, 44);

                    g2.setColor(new Color(65, 10, 35, 135));
                    int[] heights = {32 + (seed * 7) % 25, 48 + (seed * 5) % 28, 28 + (seed * 11) % 20, 55 + (seed * 3) % 25, 38 + (seed * 9) % 22};
                    int bw = w / 5;
                    for (int b = 0; b < 5; b++) {
                        g2.fillRect(b * bw + 4, h - heights[b], bw - 6, heights[b]);
                    }
                }
            };
            cityBanner.setOpaque(false);
            cityBanner.setPreferredSize(new Dimension(340, 115));
            cityBanner.setBorder(new EmptyBorder(12, 16, 12, 16));

            JLabel lblCode = new JLabel(a.getKodiIATA());
            lblCode.setForeground(Color.WHITE);
            lblCode.setFont(new Font("Segoe UI", Font.BOLD, 22));
            cityBanner.add(lblCode, BorderLayout.NORTH);

            JPanel info = new JPanel(new BorderLayout());
            info.setOpaque(false);
            info.setBorder(new EmptyBorder(12, 16, 12, 16));

            JLabel lblCity = new JLabel("<html><font color='#5C0C30' size='4'><b>" + a.getQyteti() + "</b></font>, <font color='gray'>" + a.getQyteti() + "</font><br>"
                    + "<font size='2' color='#BC246C'>" + a.getEmri() + "</font></html>");

            JPanel bottom = new JPanel(new BorderLayout());
            bottom.setOpaque(false);
            JLabel lblPrice = new JLabel("<html><font size='2' color='gray'>Nga </font><font size='4' color='#5C0C30'><b>" + String.format("%.0f", cmimiFillestar) + " €</b></font></html>");
            JButton btnSelect = new RoundedButton("Zgjidh ➔", PINK_LUX, Color.WHITE, 16);
            btnSelect.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnSelect.addActionListener(e -> {
                shfaqKartelatEFluturimeve("Të gjitha", a.getKodiIATA(), false);
                shkoTeFaqja("FLIGHTS", navButonat.get(2));
            });

            bottom.add(lblPrice, BorderLayout.WEST);
            bottom.add(btnSelect, BorderLayout.EAST);

            info.add(lblCity, BorderLayout.NORTH);
            info.add(bottom, BorderLayout.SOUTH);

            card.add(cityBanner, BorderLayout.NORTH);
            card.add(info, BorderLayout.CENTER);

            // Efekt Hover kur kalon mausi
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setBorderColor(PINK_LUX);
                    card.setBackground(BLUSH_PINK);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    card.setBorderColor(new Color(240, 200, 220));
                    card.setBackground(Color.WHITE);
                }
            });

            grid.add(card);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        page.add(head, BorderLayout.NORTH);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    // ================= FAQJA 3: FLIGHTS & BOOKING =================
    private JPanel krijoFaqenFluturimeve() {
        JPanel paneli = new JPanel(new BorderLayout(15, 15));
        paneli.setBackground(BLUSH_BG);
        paneli.setBorder(new EmptyBorder(20, 45, 20, 45));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JButton btnKthehu = new RoundedButton("← Kthehu te Home", BURGUNDY, Color.WHITE, 18);
        btnKthehu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnKthehu.addActionListener(e -> shkoTeFaqja("HOME", navButonat.get(0)));

        lblTitulliRezultateve = new JLabel("Fluturimet e Disponueshme", SwingConstants.CENTER);
        lblTitulliRezultateve.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulliRezultateve.setForeground(BURGUNDY);

        JPanel butonaDjathtas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        butonaDjathtas.setOpaque(false);

        JButton btnSortCheap = new RoundedButton("💸 Cheapest First", PINK_LUX, Color.WHITE, 18);
        btnSortCheap.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSortCheap.addActionListener(e -> shfaqKartelatEFluturimeve(nisjaFundit, mberritjaFundit, true));

        JButton btnShfaqKrejt = new RoundedButton("Të gjitha (33)", new Color(150, 45, 95), Color.WHITE, 18);
        btnShfaqKrejt.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnShfaqKrejt.addActionListener(e -> shfaqKartelatEFluturimeve("Të gjitha", "Të gjitha", false));

        butonaDjathtas.add(btnSortCheap);
        butonaDjathtas.add(btnShfaqKrejt);

        topBar.add(btnKthehu, BorderLayout.WEST);
        topBar.add(lblTitulliRezultateve, BorderLayout.CENTER);
        topBar.add(butonaDjathtas, BorderLayout.EAST);

        paneli.add(topBar, BorderLayout.NORTH);

        listaKartelaveFluturimeve = new JPanel();
        listaKartelaveFluturimeve.setLayout(new BoxLayout(listaKartelaveFluturimeve, BoxLayout.Y_AXIS));
        listaKartelaveFluturimeve.setBackground(BLUSH_BG);

        JScrollPane scroll = new JScrollPane(listaKartelaveFluturimeve);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        paneli.add(scroll, BorderLayout.CENTER);

        return paneli;
    }

    private void shfaqKartelatEFluturimeve(String nisjaFilter, String mberritjaFilter, boolean vetemTeLirat) {
        nisjaFundit = nisjaFilter;
        mberritjaFundit = mberritjaFilter;
        listaKartelaveFluturimeve.removeAll();

        ArrayList<Fluturimi> teFiltruara = new ArrayList<>();
        for (Fluturimi f : sistemi.getFluturimet()) {
            boolean okNisja = nisjaFilter.contains("Të gjitha") || nisjaFilter.startsWith(f.getNisja().getKodiIATA());
            boolean okMberritja = mberritjaFilter.contains("Të gjitha") || mberritjaFilter.startsWith(f.getMberritja().getKodiIATA());
            if (okNisja && okMberritja) {
                teFiltruara.add(f);
            }
        }

        if (vetemTeLirat) {
            teFiltruara.sort(Comparator.comparingDouble(Fluturimi::getCmimiDinamik));
        }

        for (int i = 0; i < teFiltruara.size(); i++) {
            Fluturimi f = teFiltruara.get(i);
            boolean eshteSuperCheap = vetemTeLirat && i < 5;
            listaKartelaveFluturimeve.add(krijoKarteleFluturimi(f, eshteSuperCheap));
            listaKartelaveFluturimeve.add(Box.createVerticalStrut(12));
        }

        lblTitulliRezultateve.setText((vetemTeLirat ? "💸 Fluturimet Më të Lira (" : "✈ Zgjidhni Fluturimin (") + teFiltruara.size() + " linja)");
        listaKartelaveFluturimeve.revalidate();
        listaKartelaveFluturimeve.repaint();
    }

    private JPanel krijoKarteleFluturimi(Fluturimi f, boolean badgeCheap) {
        RoundedPanel card = new RoundedPanel(24, Color.WHITE, badgeCheap ? PINK_LUX : new Color(238, 198, 218));
        card.setLayout(new BorderLayout(20, 0));
        card.setMaximumSize(new Dimension(1100, 112));
        card.setPreferredSize(new Dimension(1100, 112));
        card.setBorder(new EmptyBorder(14, 24, 14, 24));

        JPanel majtas = new JPanel();
        majtas.setOpaque(false);
        majtas.setLayout(new BoxLayout(majtas, BoxLayout.Y_AXIS));

        String tag = badgeCheap ? "  🔥 BEST VALUE" : "";
        JLabel lblKodi = new JLabel("✈ " + f.getNumriFluturimit() + " • " + f.getPortaNisjes() + tag);
        lblKodi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblKodi.setForeground(PINK_LUX);

        JLabel lblAvioni = new JLabel(f.getAvioni().getModeli() + " • Economy & Business");
        lblAvioni.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAvioni.setForeground(Color.GRAY);

        JLabel lblData = new JLabel("🕒 " + f.getDataOraNisjes());
        lblData.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblData.setForeground(BURGUNDY);

        majtas.add(lblKodi);
        majtas.add(Box.createVerticalStrut(4));
        majtas.add(lblAvioni);
        majtas.add(Box.createVerticalStrut(4));
        majtas.add(lblData);

        JPanel qendra = new JPanel(new GridLayout(1, 3, 10, 0));
        qendra.setOpaque(false);

        JLabel lblOrigin = new JLabel("<html><center><font size='5' color='#5C0C30'><b>" + f.getNisja().getKodiIATA() + "</b></font><br>"
                + "<font color='gray'>" + f.getNisja().getQyteti() + "</font></center></html>", SwingConstants.CENTER);

        int liraEcon = f.numeroUlesetELira("Economy");
        int liraBus = f.numeroUlesetELira("Business");
        JLabel lblVija = new JLabel("<html><center><font color='#EBB95C'><b>━━━━━ ✦ ━━━━━</b></font><br>"
                + "<font size='2' color='#BC246C'>Non-Stop • " + (liraEcon + liraBus) + " ulëse</font></center></html>", SwingConstants.CENTER);

        JLabel lblDest = new JLabel("<html><center><font size='5' color='#5C0C30'><b>" + f.getMberritja().getKodiIATA() + "</b></font><br>"
                + "<font color='gray'>" + f.getMberritja().getQyteti() + "</font></center></html>", SwingConstants.CENTER);

        qendra.add(lblOrigin);
        qendra.add(lblVija);
        qendra.add(lblDest);

        JPanel djathtas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        djathtas.setOpaque(false);

        JLabel lblCmimi = new JLabel("<html><right><font size='2' color='gray'>Nga</font><br>"
                + "<font size='5' color='#5C0C30'><b>" + String.format("%.2f", f.getCmimiDinamik()) + " €</b></font></right></html>");

        JButton btnZgjidh = new RoundedButton("Zgjidh & Rezervo  >", PINK_LUX, Color.WHITE, 20);
        btnZgjidh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnZgjidh.setPreferredSize(new Dimension(170, 42));
        btnZgjidh.addActionListener(e -> hapKabineneAvionit(f));

        djathtas.add(lblCmimi);
        djathtas.add(btnZgjidh);

        card.add(majtas, BorderLayout.WEST);
        card.add(qendra, BorderLayout.CENTER);
        card.add(djathtas, BorderLayout.EAST);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { card.setBackground(BLUSH_PINK); }
            @Override
            public void mouseExited(MouseEvent e) { card.setBackground(Color.WHITE); }
        });
        return card;
    }

    // ================= FAQJA 4: LUXURY 5★ HOTELS =================
    private JPanel krijoFaqenHoteleve() {
        JPanel page = new JPanel(new BorderLayout(16, 16));
        page.setBackground(BLUSH_BG);
        page.setBorder(new EmptyBorder(24, 45, 24, 45));

        JLabel tit = new JLabel("<html><font color='#5C0C30' size='5'><b>🏨 COPA AIR Luxury 5★ Partner Hotels</b></font><br>"
                + "<font color='#BC246C'>Rezervoni qëndrimin tuaj në hotelet më prestigjioze me 5 yje në destinacionet tona</font></html>");
        page.add(tit, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 22, 22));
        grid.setBackground(BLUSH_BG);

        String[][] hotelet = {
            {"The Ritz-Rose Imperial Palace", "Paris, Francë (CDG)", "185 € / natë", "⭐⭐⭐⭐⭐  Spa, Rooftop & Eiffel View"},
            {"Burj Al-Pink Royal Suites", "Dubai, EBA (DXB)", "240 € / natë", "⭐⭐⭐⭐⭐  Private Beach & Infinity Pool"},
            {"Grand Velvet Plaza & Spa", "Vjenë, Austri (VIE)", "155 € / natë", "⭐⭐⭐⭐⭐  Historic Center & Fine Dining"},
            {"Bosphorus Rose Mansion", "Stamboll, Turqi (IST)", "145 € / natë", "⭐⭐⭐⭐⭐  Sea View & Ottoman Hammam"},
            {"Manhattan Champagne Tower", "Nju Jork, SHBA (JFK)", "290 € / natë", "⭐⭐⭐⭐⭐  5th Avenue VIP Penthouse"},
            {"Swiss Rose Alpine Resort", "Cyrih, Zvicër (ZRH)", "210 € / natë", "⭐⭐⭐⭐⭐  Lake View & Thermal Spa"}
        };

        for (int i = 0; i < hotelet.length; i++) {
            String[] h = hotelet[i];
            final int idx = i;
            RoundedPanel card = new RoundedPanel(26, Color.WHITE, CHAMPAGNE_GOLD);
            card.setLayout(new BorderLayout());

            JPanel visual = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(new GradientPaint(0, 0, BURGUNDY, getWidth(), getHeight(), idx % 2 == 0 ? PINK_LUX : CHAMPAGNE_GOLD));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight() + 15, 26, 26);

                    g2.setColor(new Color(255, 255, 255, 45));
                    g2.fillRect(30, 35, getWidth() - 60, getHeight() - 35);
                    g2.setColor(CHAMPAGNE_GOLD);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    g2.drawString("★★★★★  5-STAR LUXURY COLLECTION", 22, 26);
                }
            };
            visual.setOpaque(false);
            visual.setPreferredSize(new Dimension(340, 110));

            JPanel body = new JPanel(new BorderLayout(8, 8));
            body.setOpaque(false);
            body.setBorder(new EmptyBorder(14, 18, 14, 18));

            JLabel info = new JLabel("<html><font color='#5C0C30' size='4'><b>" + h[0] + "</b></font><br>"
                    + "<font color='gray'>📍 " + h[1] + "</font><br>"
                    + "<font color='#BC246C' size='2'>" + h[3] + "</font></html>");

            JPanel bottom = new JPanel(new BorderLayout());
            bottom.setOpaque(false);
            JLabel price = new JLabel("<html><font size='4' color='#5C0C30'><b>" + h[2] + "</b></font></html>");
            JButton btnBookHotel = new RoundedButton("Rezervo Hotelin", PINK_LUX, Color.WHITE, 18);
            btnBookHotel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnBookHotel.addActionListener(e -> {
                shfaqMesazhModern("Rezervim Hoteli 5★",
                    "Hoteli luksoz '" + h[0] + "' (" + h[1] + ") është i disponueshëm!\n"
                    + "Për ta kombinuar me fluturimin tuaj, zgjidhni fluturimin drejt " + h[1] + " dhe në Hapin 2 zgjidhni kategorinë 5 Yje!");
            });

            bottom.add(price, BorderLayout.WEST);
            bottom.add(btnBookHotel, BorderLayout.EAST);

            body.add(info, BorderLayout.CENTER);
            body.add(bottom, BorderLayout.SOUTH);

            card.add(visual, BorderLayout.NORTH);
            card.add(body, BorderLayout.CENTER);
            grid.add(card);
        }

        page.add(grid, BorderLayout.CENTER);
        return page;
    }

    // ================= FAQJA 5: PINK VIP MEMBERSHIP =================
    private JPanel krijoFaqenPinkVip() {
        JPanel page = new JPanel(new BorderLayout(20, 20));
        page.setBackground(BLUSH_BG);
        page.setBorder(new EmptyBorder(28, 55, 28, 55));

        JLabel tit = new JLabel("<html><center><font color='#5C0C30' size='6'><b>👑 COPA AIR PINK VIP CLUB</b></font><br>"
                + "<font color='#BC246C' size='4'>Zgjidhni statusin tuaj ekskluziv dhe përfitoni pikë, lounge VIP dhe zbritje në çdo fluturim</font></center></html>", SwingConstants.CENTER);
        page.add(tit, BorderLayout.NORTH);

        JPanel tiersGrid = new JPanel(new GridLayout(1, 3, 28, 0));
        tiersGrid.setOpaque(false);

        tiersGrid.add(krijoKarteleVip(
            "PINK SILVER", "0 – 10,000 Pikë", "10% Zbritje",
            new Color(145, 145, 160), Color.WHITE,
            "✦ 10% zbritje me kodin COPA20\n✦ Prioritet në Online Check-in\n✦ 1 Valixhe dore falas (10kg)\n✦ Mbledhje 1.5x pikë për çdo fluturim"
        ));

        tiersGrid.add(krijoKarteleVip(
            "PINK GOLD", "10,000 – 35,000 Pikë", "15% Zbritje",
            CHAMPAGNE_GOLD, BURGUNDY,
            "✦ 15% zbritje në fluturime & hotele 5★\n✦ Hyrje falas në Pink Champagne Lounge\n✦ Valixhe 23kg + Zgjedhje ulëseje falas\n✦ Mbledhje 2.5x pikë VIP"
        ));

        tiersGrid.add(krijoKarteleVip(
            "PINK DIAMOND", "35,000+ Pikë VIP", "20% Zbritje + Upgrade",
            BURGUNDY, CHAMPAGNE_GOLD,
            "✦ 20% zbritje në çdo destinacion botëror\n✦ Upgrade falas në Business Class\n✦ Limuzinë private nga/për në aeroport\n✦ 2 Valixhe 32kg + Asistencë VIP 24/7"
        ));

        page.add(tiersGrid, BorderLayout.CENTER);
        return page;
    }

    private JPanel krijoKarteleVip(String niveli, String piket, String zbritja, Color headerColor, Color textHeader, String perfitimet) {
        RoundedPanel card = new RoundedPanel(30, Color.WHITE, headerColor);
        card.setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setBackground(headerColor);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(22, 20, 22, 20));

        JLabel l1 = new JLabel("👑 " + niveli);
        l1.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l1.setForeground(textHeader);
        l1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel l2 = new JLabel(piket);
        l2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l2.setForeground(textHeader);
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel l3 = new JLabel(zbritja);
        l3.setFont(new Font("Segoe UI", Font.BOLD, 18));
        l3.setForeground(textHeader);
        l3.setAlignmentX(Component.CENTER_ALIGNMENT);

        top.add(l1);
        top.add(Box.createVerticalStrut(6));
        top.add(l2);
        top.add(Box.createVerticalStrut(8));
        top.add(l3);

        JTextArea body = new JTextArea(perfitimet);
        body.setEditable(false);
        body.setOpaque(false);
        body.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        body.setForeground(BURGUNDY);
        body.setBorder(new EmptyBorder(22, 24, 16, 24));

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.CENTER));
        foot.setOpaque(false);
        foot.setBorder(new EmptyBorder(0, 0, 18, 0));
        JButton btnJoin = new RoundedButton("Aktivizo " + niveli, PINK_LUX, Color.WHITE, 20);
        btnJoin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnJoin.setPreferredSize(new Dimension(210, 40));
        btnJoin.addActionListener(e -> shfaqMesazhModern("Mirë se vini në " + niveli,
            "Anëtarësimi juaj '" + niveli + "' u aktivizua! Përdorni kodin COPA20 në kërkim për të përfituar zbritjen."));
        foot.add(btnJoin);

        card.add(top, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(foot, BorderLayout.SOUTH);
        return card;
    }

    // ================= FAQJA 6: ONLINE CHECK-IN (PNR + MBIEMRI) =================
    private JPanel krijoFaqenOnlineCheckIn() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(BLUSH_BG);

        RoundedPanel card = new RoundedPanel(30, Color.WHITE, PINK_LUX);
        card.setLayout(new BorderLayout(16, 16));
        card.setPreferredSize(new Dimension(540, 380));
        card.setBorder(new EmptyBorder(28, 34, 28, 34));

        JLabel tit = new JLabel("<html><font color='#5C0C30' size='5'><b>📱 COPA AIR Online Check-in</b></font><br>"
                + "<font color='#BC246C'>Shkruani Kodin e Rezervimit (PNR) dhe Mbiemrin e udhëtarit</font></html>");
        card.add(tit, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 1, 8, 8));
        form.setOpaque(false);

        JTextField txtPnr = new JTextField();
        txtPnr.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JTextField txtMbiemri = new JTextField();
        txtMbiemri.setFont(new Font("Segoe UI", Font.BOLD, 15));

        form.add(new JLabel("🔖 Numri i Rezervimit (PNR Code, p.sh. CM4521):"));
        form.add(txtPnr);
        form.add(new JLabel("👤 Mbiemri i Pasagjerit:"));
        form.add(txtMbiemri);

        card.add(form, BorderLayout.CENTER);

        JButton btnCheckIn = new RoundedButton("✔ Kontrollo Fluturimin & Hap Boarding Pass", PINK_LUX, Color.WHITE, 22);
        btnCheckIn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCheckIn.setPreferredSize(new Dimension(460, 44));
        btnCheckIn.addActionListener(e -> {
            Bileta b = sistemi.gjejBiletePerCheckIn(txtPnr.getText(), txtMbiemri.getText());
            if (b == null) {
                shfaqMesazhModern("Nuk u gjet", "Nuk u gjet asnjë rezervim aktiv me këtë PNR dhe mbiemër.\nKontrolloni kodin PNR te 'My Trips' dhe provoni përsëri!");
                return;
            }
            b.kryejCheckIn();
            rifreskoMyTrips();
            shfaqBoardingPassReal(b);
        });

        card.add(btnCheckIn, BorderLayout.SOUTH);
        page.add(card);
        return page;
    }

    // ================= FAQJA 7: FAQJA E KONFIRMIMIT PAS REZERVIMIT =================
    private JPanel krijoFaqenKonfirmimit() {
        paneliKonfirmimit = new JPanel(new GridBagLayout());
        paneliKonfirmimit.setBackground(BLUSH_BG);
        return paneliKonfirmimit;
    }

    private void shfaqFaqenEKonfirmimit(Bileta b) {
        paneliKonfirmimit.removeAll();

        RoundedPanel card = new RoundedPanel(32, Color.WHITE, CHAMPAGNE_GOLD);
        card.setLayout(new BorderLayout(18, 18));
        card.setPreferredSize(new Dimension(680, 450));
        card.setBorder(new EmptyBorder(30, 36, 30, 36));

        JLabel top = new JLabel("<html><center><font color='#28A745' size='6'><b>✔ REZERVIMI U KONFIRMUA ME SUKSES!</b></font><br>"
                + "<font color='#5C0C30' size='4'>Faleminderit që zgjodhët <b>COPA AIR Pink Ombré Luxury Edition</b></font></center></html>", SwingConstants.CENTER);

        JPanel details = new JPanel(new GridLayout(5, 2, 12, 10));
        details.setBackground(BLUSH_PINK);
        details.setBorder(new EmptyBorder(18, 22, 18, 22));

        details.add(new JLabel("🔖 Kodi i Rezervimit (PNR):"));
        details.add(new JLabel("<html><font color='#BC246C' size='4'><b>" + b.getKodiRezervimit() + "</b></font></html>"));
        details.add(new JLabel("👤 Pasagjeri:"));
        details.add(new JLabel("<b>" + b.getPasagjeri().getEmriPlote() + "</b>"));
        details.add(new JLabel("✈ Fluturimi & Orari:"));
        details.add(new JLabel("<b>" + b.getFluturimi().getNumriFluturimit() + " • " + b.getFluturimi().getDataOraNisjes() + "</b>"));
        details.add(new JLabel("💺 Ulësja & Paketa/Hoteli:"));
        details.add(new JLabel("<b>" + b.getUlesja().getNumriUleses() + " • " + b.getPaketa() + "</b>"));
        details.add(new JLabel("💳 Shuma e Paguar:"));
        details.add(new JLabel("<b>" + String.format("%.2f", b.getPagesa().getShumaTotale()) + " EUR (" + b.getPagesa().getMenyraPageses() + ")</b>"));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        buttons.setOpaque(false);

        JButton btnBoarding = new RoundedButton("🎫 Shiko & Printo Boarding Pass", PINK_LUX, Color.WHITE, 20);
        btnBoarding.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBoarding.setPreferredSize(new Dimension(245, 42));
        btnBoarding.addActionListener(e -> shfaqBoardingPassReal(b));

        JButton btnMyTrips = new RoundedButton("📋 Shko te My Trips", BURGUNDY, Color.WHITE, 20);
        btnMyTrips.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnMyTrips.setPreferredSize(new Dimension(185, 42));
        btnMyTrips.addActionListener(e -> {
            rifreskoMyTrips();
            shkoTeFaqja("MY_TRIPS", navButonat.get(6));
        });

        buttons.add(btnBoarding);
        buttons.add(btnMyTrips);

        card.add(top, BorderLayout.NORTH);
        card.add(details, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);

        paneliKonfirmimit.add(card);
        paneliKonfirmimit.revalidate();
        paneliKonfirmimit.repaint();
        cardLayout.show(qendraKardave, "CONFIRMATION");
    }

    // ================= FAQJA 8: MY TRIPS =================
    private JPanel krijoFaqenMyTrips() {
        JPanel p = new JPanel(new BorderLayout(12, 12));
        p.setBackground(BLUSH_BG);
        p.setBorder(new EmptyBorder(20, 45, 20, 45));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel tit = new JLabel("🎫 My Trips (Udhëtimet, Statusi i Rezervimit & Boarding Pass)");
        tit.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tit.setForeground(BURGUNDY);

        JPanel butonaHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        butonaHeader.setOpaque(false);

        JButton btnPrintoFundit = new RoundedButton("🖨 Printo Biletën e Fundit", PINK_LUX, Color.WHITE, 18);
        btnPrintoFundit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPrintoFundit.addActionListener(e -> {
            Bileta b = sistemi.merrBiletenEFundit();
            if (b == null) shfaqMesazhModern("Nuk ka bileta", "Ende nuk keni rezervuar asnjë biletë!");
            else shfaqBoardingPassReal(b);
        });

        JButton btnAnulo = new RoundedButton("❌ Anulo Biletë me PNR", E_KUQE_ZENE, Color.WHITE, 18);
        btnAnulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAnulo.addActionListener(e -> {
            String pnr = shfaqInputModern("Anulimi i Biletës", "Shkruani Kodin PNR për anulim:");
            if (pnr != null && sistemi.anuloRezervimin(pnr)) {
                rifreskoMyTrips();
                shfaqMesazhModern("Sukses", "Rezervimi u anulua!");
            }
        });

        butonaHeader.add(btnPrintoFundit);
        butonaHeader.add(btnAnulo);

        header.add(tit, BorderLayout.WEST);
        header.add(butonaHeader, BorderLayout.EAST);
        p.add(header, BorderLayout.NORTH);

        listaKartelaveBiletave = new JPanel();
        listaKartelaveBiletave.setLayout(new BoxLayout(listaKartelaveBiletave, BoxLayout.Y_AXIS));
        listaKartelaveBiletave.setBackground(BLUSH_BG);

        JScrollPane sc = new JScrollPane(listaKartelaveBiletave);
        sc.setBorder(null);
        p.add(sc, BorderLayout.CENTER);
        return p;
    }

    private void rifreskoMyTrips() {
        if (listaKartelaveBiletave == null) return;
        listaKartelaveBiletave.removeAll();
        int nr = 0;
        for (Bileta b : sistemi.getTeGjithaBiletat()) {
            if (b.isEshteAktiv()) {
                nr++;
                RoundedPanel c = new RoundedPanel(22, Color.WHITE, PINK_SOFT);
                c.setLayout(new BorderLayout(15, 0));
                c.setMaximumSize(new Dimension(1100, 96));
                c.setBorder(new EmptyBorder(12, 22, 12, 22));

                String statusi = b.isCheckInKryer() ? "✔ CHECKED-IN" : "● CONFIRMED";
                JLabel info = new JLabel("<html><font color='#5C0C30' size='4'><b>PNR: " + b.getKodiRezervimit() + "</b></font>  •  <font color='#28A745'><b>" + statusi + "</b></font><br>"
                        + "<b>" + b.getFluturimi().getNisja().getQyteti() + " ➔ " + b.getFluturimi().getMberritja().getQyteti() + "</b> (" + b.getFluturimi().getDataOraNisjes() + ")<br>"
                        + "Pasagjeri: <b>" + b.getPasagjeri().getEmriPlote() + "</b>  •  Ulësja: <b>" + b.getUlesja().getNumriUleses() + "</b>  •  Paketa: <b>" + b.getPaketa() + "</b></html>");

                JButton btnBP = new RoundedButton("🎫 Boarding Pass & Copy PNR", PINK_LUX, Color.WHITE, 18);
                btnBP.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnBP.addActionListener(e -> shfaqBoardingPassReal(b));

                c.add(info, BorderLayout.CENTER);
                c.add(btnBP, BorderLayout.EAST);
                listaKartelaveBiletave.add(c);
                listaKartelaveBiletave.add(Box.createVerticalStrut(10));
            }
        }
        if (nr == 0) {
            JLabel bosh = new JLabel("Ende nuk keni rezervime aktive. Eksploroni 33 destinacionet për të rezervuar!", SwingConstants.CENTER);
            bosh.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            listaKartelaveBiletave.add(bosh);
        }
        listaKartelaveBiletave.revalidate();
        listaKartelaveBiletave.repaint();
    }

    // ================= FAQJA 9: ADMIN PANEL =================
    private JPanel krijoFaqenAdmin() {
        JPanel p = new JPanel(new BorderLayout(14, 14));
        p.setBackground(BLUSH_BG);
        p.setBorder(new EmptyBorder(22, 45, 22, 45));

        RoundedPanel bar = new RoundedPanel(20, Color.WHITE, PINK_SOFT);
        bar.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 10));

        JButton btnShto = new RoundedButton("+ Shto Fluturim", PINK_LUX, Color.WHITE, 18);
        JButton btnCmimi = new RoundedButton("✏ Ndrysho Çmimin e Fluturimit", BURGUNDY, Color.WHITE, 18);
        JButton btnRezervimet = new RoundedButton("📋 Të Gjitha Rezervimet", new Color(150, 45, 95), Color.WHITE, 18);
        JButton btnFitimet = new RoundedButton("💰 Të Ardhurat Totale", new Color(195, 135, 35), Color.WHITE, 18);
        JButton btnEksporto = new RoundedButton("📄 Eksporto Raportin", new Color(40, 140, 85), Color.WHITE, 18);

        bar.add(btnShto); bar.add(btnCmimi); bar.add(btnRezervimet); bar.add(btnFitimet); bar.add(btnEksporto);

        JTextArea log = new JTextArea();
        log.setEditable(false);
        log.setFont(new Font("Consolas", Font.PLAIN, 13));
        log.setText("COPA AIR Admin Control Center:\n- 33 Destinacione dhe " + sistemi.getFluturimet().size() + " linja aktive.");

        btnShto.addActionListener(e -> hapDritarenShtoFluturim());
        btnCmimi.addActionListener(e -> {
            String nr = shfaqInputModern("Ndrysho Çmimin", "Shkruani kodin e fluturimit (p.sh. CM-101):");
            if (nr != null) {
                Fluturimi f = sistemi.gjejFluturim(nr);
                if (f != null) {
                    String cm = shfaqInputModern("Çmimi i Ri", "Shkruani çmimin e ri në EUR për " + f.getNumriFluturimit() + ":");
                    if (cm != null) {
                        try {
                            f.setCmimiBaze(Double.parseDouble(cm.trim()));
                            log.setText("Çmimi për fluturimin " + f.getNumriFluturimit() + " u përditësua në " + cm + " EUR!");
                        } catch (Exception ex) {
                            shfaqMesazhModern("Gabim", "Ju lutem shkruani një numër të vlefshëm!");
                        }
                    }
                } else {
                    shfaqMesazhModern("Gabim", "Fluturimi nuk u gjet!");
                }
            }
        });
        btnRezervimet.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("=== LISTA E TË GJITHA REZERVIMEVE ===\n\n");
            for (Bileta b : sistemi.getTeGjithaBiletat()) {
                sb.append("PNR: ").append(b.getKodiRezervimit())
                  .append(" | Pasagjeri: ").append(b.getPasagjeri().getEmriPlote())
                  .append(" | Linja: ").append(b.getFluturimi().getNumriFluturimit())
                  .append(" | Statusi: ").append(b.isEshteAktiv() ? "AKTIV" : "ANULUAR").append("\n");
            }
            log.setText(sb.toString());
        });
        btnFitimet.addActionListener(e -> log.setText("Të ardhurat totale të COPA AIR: " + String.format("%.2f", sistemi.llogaritTeArdhuratTotale()) + " EUR"));
        btnEksporto.addActionListener(e -> {
            sistemi.eksportoRaportin();
            shfaqMesazhModern("Eksportuar", "Raporti u ruajt në 'raporti_kompanise.txt'!");
        });

        p.add(bar, BorderLayout.NORTH);
        p.add(new JScrollPane(log), BorderLayout.CENTER);
        return p;
    }

    // ================= FOOTER PROFESIONAL =================
    private JPanel krijoFooterinProfesional() {
        JPanel footer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, BURGUNDY, getWidth(), 0, new Color(130, 20, 68)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        footer.setBorder(new EmptyBorder(10, 36, 10, 36));

        JLabel left = new JLabel("<html><font color='#EBB95C'><b>✦ COPA AIR</b></font> <font color='#FFEEF5'>| About Us: Kompania juaj luksoze ajrore me 33 destinacione botërore & hotele 5★.</font></html>");
        JLabel right = new JLabel("<html><font color='#FFEEF5'>📞 Contact Us: +383 38 700 800  •  ✉ vip@copaair.com  •  🌐 Instagram / TikTok / X: @CopaAirLuxury</font></html>");

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    // ================= KABINA 30 RRESHTA, HOTELI 5★, PAGESA & BOARDING PASS =================
    private void hapKabineneAvionit(Fluturimi f) {
        JDialog dritarjaAvionit = new JDialog(this, "COPA AIR Pink Cabin (30 Rreshta / 180 Ulëse) - " + f.getNumriFluturimit(), true);
        dritarjaAvionit.setSize(940, 760);
        dritarjaAvionit.setLocationRelativeTo(this);
        dritarjaAvionit.setLayout(new BorderLayout());

        JPanel koka = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, BURGUNDY, getWidth(), 0, PINK_LUX));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        koka.setBorder(new EmptyBorder(10, 18, 10, 18));
        JLabel info = new JLabel("<html><center><font color='white' size='4'><b>✦ KABINA LUKSOZE (" + f.getAvioni().getModeli() + ")  •  30 RRESHTA (1A–30F)</b></font><br>"
                + "<font color='#EBB95C'>■ Business Class (1–3)</font> &nbsp;&nbsp; "
                + "<font color='#FF99CC'>■ Extra Legroom (12–13, 30)</font> &nbsp;&nbsp; "
                + "<font color='#FFEEF5'>■ Economy Class</font> &nbsp;&nbsp; "
                + "<font color='#FF5577'>■ E Zënë</font></center></html>", SwingConstants.CENTER);
        koka.add(info, BorderLayout.CENTER);
        dritarjaAvionit.add(koka, BorderLayout.NORTH);

        final Ulesja[] ulesjaEZgjedhur = {null};
        JPanel paneliDjathtas = new JPanel();
        paneliDjathtas.setPreferredSize(new Dimension(290, 720));
        paneliDjathtas.setBackground(Color.WHITE);
        paneliDjathtas.setBorder(new EmptyBorder(18, 18, 18, 18));
        paneliDjathtas.setLayout(new BoxLayout(paneliDjathtas, BoxLayout.Y_AXIS));

        JLabel lblTitulliInspector = new JLabel("💺 DETAJET E ULËSES");
        lblTitulliInspector.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulliInspector.setForeground(BURGUNDY);

        JLabel lblNumriUleses = new JLabel("Zgjidhni një ulëse");
        lblNumriUleses.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNumriUleses.setForeground(PINK_LUX);

        JLabel lblPozicioni = new JLabel("Pozicioni: —");
        JLabel lblKategoria = new JLabel("Klasa: —");
        JLabel lblCmimiUleses = new JLabel("Çmimi: " + String.format("%.2f", f.getCmimiDinamik()) + " EUR");
        lblCmimiUleses.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblCmimiUleses.setForeground(new Color(34, 153, 84));

        JButton btnVazhdoRezervimin = new RoundedButton("Konfirmo Ulësen & Vazhdo  >", PINK_LUX, Color.WHITE, 20);
        btnVazhdoRezervimin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVazhdoRezervimin.setEnabled(false);
        btnVazhdoRezervimin.addActionListener(e -> {
            if (ulesjaEZgjedhur[0] != null) {
                dritarjaAvionit.dispose();
                hapZgjidhPaketenDheHotelin(f, ulesjaEZgjedhur[0]);
            }
        });

        JButton btnAuto = new RoundedButton("🎲 Zgjidh Ulëse Dritareje", BURGUNDY, Color.WHITE, 20);

        paneliDjathtas.add(lblTitulliInspector);
        paneliDjathtas.add(Box.createVerticalStrut(12));
        paneliDjathtas.add(lblNumriUleses);
        paneliDjathtas.add(Box.createVerticalStrut(8));
        paneliDjathtas.add(lblPozicioni);
        paneliDjathtas.add(Box.createVerticalStrut(4));
        paneliDjathtas.add(lblKategoria);
        paneliDjathtas.add(Box.createVerticalStrut(10));
        paneliDjathtas.add(lblCmimiUleses);
        paneliDjathtas.add(Box.createVerticalStrut(20));
        paneliDjathtas.add(btnVazhdoRezervimin);
        paneliDjathtas.add(Box.createVerticalStrut(10));
        paneliDjathtas.add(btnAuto);

        dritarjaAvionit.add(paneliDjathtas, BorderLayout.EAST);

        JPanel trupiAeroplanit = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight(), cx = w / 2;

                g2.setPaint(new GradientPaint(0, 0, new Color(252, 228, 240), 0, h, new Color(243, 195, 220)));
                g2.fillRect(0, 0, w, h);

                g2.setPaint(new GradientPaint(cx, 15, Color.WHITE, cx, h - 45, BLUSH_PINK));
                g2.fillRoundRect(cx - 200, 15, 400, h - 45, 360, 320);
                g2.setColor(PINK_SOFT);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(cx - 200, 15, 400, h - 45, 360, 320);
            }
        };

        JPanel kornizaKabinës = new JPanel();
        kornizaKabinës.setOpaque(false);
        kornizaKabinës.setLayout(new BoxLayout(kornizaKabinës, BoxLayout.Y_AXIS));
        kornizaKabinës.setBorder(new EmptyBorder(85, 0, 75, 0));

        ArrayList<Ulesja> uleset = f.getUleset();
        ArrayList<JButton> teGjithaButonat = new ArrayList<>();
        String[] shkronjatKol = {"A", "B", "C", "D", "E", "F"};

        for (int i = 0; i < uleset.size(); i += 6) {
            int nrRreshtit = (i / 6) + 1;
            JPanel rreshtUlesesh = new JPanel(new GridLayout(1, 7, 4, 4));
            rreshtUlesesh.setOpaque(false);
            rreshtUlesesh.setMaximumSize(new Dimension(345, 34));

            for (int kol = 0; kol < 7; kol++) {
                if (kol == 3) {
                    JLabel korridori = new JLabel(String.valueOf(nrRreshtit), SwingConstants.CENTER);
                    korridori.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    korridori.setForeground(BURGUNDY);
                    rreshtUlesesh.add(korridori);
                } else {
                    int idxNeRresht = (kol < 3) ? kol : kol - 1;
                    int idxUleses = i + idxNeRresht;
                    if (idxUleses < uleset.size()) {
                        Ulesja u = uleset.get(idxUleses);
                        String etiketaVizuale = nrRreshtit + shkronjatKol[idxNeRresht];
                        JButton btn = new JButton(etiketaVizuale);
                        btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
                        btn.setMargin(new Insets(1, 1, 1, 1));
                        btn.setFocusPainted(false);

                        if (u.isEshteEZene()) {
                            btn.setBackground(E_KUQE_ZENE);
                            btn.setForeground(Color.WHITE);
                            btn.setEnabled(false);
                        } else if (u.getKlasa().equalsIgnoreCase("Business") || nrRreshtit <= 3) {
                            btn.setBackground(CHAMPAGNE_GOLD);
                            btn.setForeground(BURGUNDY);
                        } else if (nrRreshtit == 12 || nrRreshtit == 13 || nrRreshtit == 30) {
                            btn.setBackground(new Color(255, 165, 210));
                            btn.setForeground(BURGUNDY);
                        } else {
                            btn.setBackground(BLUSH_PINK);
                            btn.setForeground(BURGUNDY);
                        }

                        if (!u.isEshteEZene()) {
                            teGjithaButonat.add(btn);
                            btn.addActionListener(e -> {
                                ulesjaEZgjedhur[0] = u;
                                lblNumriUleses.setText("Ulësja: " + etiketaVizuale);
                                lblPozicioni.setText("Pozicioni: " + ((etiketaVizuale.endsWith("A") || etiketaVizuale.endsWith("F")) ? "Dritare" : "Korridor/Mes"));
                                lblKategoria.setText("Klasa: " + (nrRreshtit <= 3 ? "Business Class" : "Economy Class"));
                                double cmimi = f.getCmimiDinamik() + (nrRreshtit <= 3 ? 45 : 0);
                                lblCmimiUleses.setText("Çmimi: " + String.format("%.2f", cmimi) + " EUR");
                                btnVazhdoRezervimin.setEnabled(true);
                            });
                        }
                        rreshtUlesesh.add(btn);
                    }
                }
            }
            kornizaKabinës.add(rreshtUlesesh);
            kornizaKabinës.add(Box.createVerticalStrut(4));
        }

        btnAuto.addActionListener(e -> {
            for (JButton b : teGjithaButonat) {
                if (b.getText().endsWith("A") || b.getText().endsWith("F")) { b.doClick(); return; }
            }
        });

        trupiAeroplanit.add(kornizaKabinës, BorderLayout.CENTER);
        JScrollPane scrollKabina = new JScrollPane(trupiAeroplanit);
        scrollKabina.getVerticalScrollBar().setUnitIncrement(16);
        dritarjaAvionit.add(scrollKabina, BorderLayout.CENTER);
        dritarjaAvionit.setVisible(true);
    }

    private void hapZgjidhPaketenDheHotelin(Fluturimi f, Ulesja ulesja) {
        JDialog dritarjaPaketa = new JDialog(this, "COPA AIR  |  Hapi 2/3: Tarifa, Bagazhi & Hotelet 5★", true);
        dritarjaPaketa.setSize(850, 660);
        dritarjaPaketa.setLocationRelativeTo(this);
        dritarjaPaketa.setLayout(new BorderLayout());
        dritarjaPaketa.getContentPane().setBackground(BLUSH_BG);

        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, BURGUNDY, getWidth(), 0, PINK_LUX));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setBorder(new EmptyBorder(14, 24, 14, 24));
        header.add(new JLabel("<html><font color='#EBB95C' size='4'><b>✦ ZGJIDHNI TARIFËN & HOTELIN NË " + f.getMberritja().getQyteti().toUpperCase() + "</b></font><br>"
                + "<font color='white'>Fluturimi <b>" + f.getNumriFluturimit() + "</b> • Ulësja: <b>" + ulesja.getNumriUleses() + "</b></font></html>"), BorderLayout.WEST);
        dritarjaPaketa.add(header, BorderLayout.NORTH);

        JPanel qendra = new JPanel();
        qendra.setLayout(new BoxLayout(qendra, BoxLayout.Y_AXIS));
        qendra.setBackground(BLUSH_BG);
        qendra.setBorder(new EmptyBorder(16, 24, 14, 24));

        JPanel kartelatPanel = new JPanel(new GridLayout(1, 3, 14, 0));
        kartelatPanel.setOpaque(false);
        kartelatPanel.setMaximumSize(new Dimension(790, 135));

        final double[] kostoPaketa = {0.0};
        final String[] emriPaketes = {"Economy Basic"};

        RoundedPanel cardBasic = krijoKarteleTarife("ECONOMY BASIC", "0.00 €", "✔ 1 Çantë dore (8kg)\n✔ Vend standard", true);
        RoundedPanel cardClassic = krijoKarteleTarife("ECONOMY CLASSIC", "+25.00 €", "✔ Valixhe 23kg\n✔ Ndryshim bilete i lehtë", false);
        RoundedPanel cardFull = krijoKarteleTarife("PINK VIP FULL", "+50.00 €", "✔ 2 Valixhe (32kg)\n✔ Priority & Rimbursim 100%", false);

        kartelatPanel.add(cardBasic); kartelatPanel.add(cardClassic); kartelatPanel.add(cardFull);
        qendra.add(kartelatPanel);
        qendra.add(Box.createVerticalStrut(12));

        RoundedPanel hotelPanel = new RoundedPanel(20, Color.WHITE, PINK_SOFT);
        hotelPanel.setLayout(new GridLayout(3, 2, 12, 8));
        hotelPanel.setMaximumSize(new Dimension(790, 145));
        hotelPanel.setBorder(new EmptyBorder(10, 16, 10, 16));

        JComboBox<String> comboYjet = new JComboBox<>(new String[]{
            "Të gjitha kategoritë (3★ - 5★)",
            "⭐⭐⭐⭐⭐  5 Yje (Luxury & Spa)",
            "⭐⭐⭐⭐  4 Yje (Superior Boutique)",
            "⭐⭐⭐  3 Yje (City Comfort)"
        });
        JComboBox<String> comboHoteli = new JComboBox<>();
        String qyteti = f.getMberritja().getQyteti();
        Runnable ngarkoHotelet = () -> {
            comboHoteli.removeAllItems();
            comboHoteli.addItem("Pa hotel (Vetëm fluturimi) - 0 €/natë");
            int zg = comboYjet.getSelectedIndex();
            if (zg == 0 || zg == 1) {
                comboHoteli.addItem("⭐⭐⭐⭐⭐ " + qyteti + " Grand Imperial Palace & Spa - 145 €/natë");
                comboHoteli.addItem("⭐⭐⭐⭐⭐ The Ritz-Rose Luxury Suites " + qyteti + " - 165 €/natë");
            }
            if (zg == 0 || zg == 2) {
                comboHoteli.addItem("⭐⭐⭐⭐ " + qyteti + " Boutique Skyline Hotel - 85 €/natë");
                comboHoteli.addItem("⭐⭐⭐⭐ Copa Plaza Central " + qyteti + " - 92 €/natë");
            }
            if (zg == 0 || zg == 3) {
                comboHoteli.addItem("⭐⭐⭐ " + qyteti + " Rose Garden Inn - 52 €/natë");
                comboHoteli.addItem("⭐⭐⭐ " + qyteti + " Express Airport Hotel - 45 €/natë");
            }
        };
        ngarkoHotelet.run();
        JSpinner spinNetet = new JSpinner(new SpinnerNumberModel(2, 1, 14, 1));

        hotelPanel.add(new JLabel("🏨 Me sa yje e dëshironi hotelin? (Deri 5★):"));
        hotelPanel.add(comboYjet);
        hotelPanel.add(new JLabel("🛏 Zgjidhni Hotelin në " + qyteti + " (6 opsione):"));
        hotelPanel.add(comboHoteli);
        hotelPanel.add(new JLabel("🌙 Numri i netëve:"));
        hotelPanel.add(spinNetet);
        qendra.add(hotelPanel);
        qendra.add(Box.createVerticalStrut(12));

        JPanel addonsPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        addonsPanel.setOpaque(false);
        addonsPanel.setMaximumSize(new Dimension(790, 56));
        JCheckBox chkPriority = stilezoAddon("⚡ Priority Boarding", "+10.00 €");
        JCheckBox chkUshqim = stilezoAddon("🥂 Pink Champagne & Menu", "+12.00 €");
        JCheckBox chkSigurim = stilezoAddon("🛡 Sigurim Udhëtimi", "+8.00 €");
        addonsPanel.add(chkPriority); addonsPanel.add(chkUshqim); addonsPanel.add(chkSigurim);
        qendra.add(addonsPanel);

        dritarjaPaketa.add(qendra, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(12, 24, 12, 24));
        JLabel lblTotali = new JLabel("Totali: " + String.format("%.2f", f.getCmimiDinamik()) + " EUR");
        lblTotali.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTotali.setForeground(BURGUNDY);

        Runnable perditesoTotalin = () -> {
            String sel = comboHoteli.getSelectedItem() != null ? comboHoteli.getSelectedItem().toString() : "";
            double hCmim = sel.contains("145 €") ? 145 : sel.contains("165 €") ? 165 : sel.contains("85 €") ? 85 : sel.contains("92 €") ? 92 : sel.contains("52 €") ? 52 : sel.contains("45 €") ? 45 : 0;
            double shtese = kostoPaketa[0] + (hCmim * (int) spinNetet.getValue())
                    + (chkPriority.isSelected() ? 10 : 0) + (chkUshqim.isSelected() ? 12 : 0) + (chkSigurim.isSelected() ? 8 : 0);
            lblTotali.setText("Totali (Fluturim + Hotel): " + String.format("%.2f", f.getCmimiDinamik() + shtese) + " EUR");
        };

        comboYjet.addActionListener(e -> { ngarkoHotelet.run(); perditesoTotalin.run(); });
        comboHoteli.addActionListener(e -> perditesoTotalin.run());
        spinNetet.addChangeListener(e -> perditesoTotalin.run());
        chkPriority.addActionListener(e -> perditesoTotalin.run());
        chkUshqim.addActionListener(e -> perditesoTotalin.run());
        chkSigurim.addActionListener(e -> perditesoTotalin.run());

        RoundedPanel[] kartelat = {cardBasic, cardClassic, cardFull};
        double[] cmimetK = {0.0, 25.0, 50.0};
        String[] emratK = {"Economy Basic", "Economy Classic", "Pink VIP Full"};
        for (int i = 0; i < 3; i++) {
            final int idx = i;
            kartelat[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    kostoPaketa[0] = cmimetK[idx]; emriPaketes[0] = emratK[idx];
                    for (int j = 0; j < 3; j++) {
                        kartelat[j].setBorderColor(j == idx ? PINK_LUX : new Color(235, 195, 215));
                        kartelat[j].setBackground(j == idx ? BLUSH_PINK : Color.WHITE);
                    }
                    perditesoTotalin.run();
                }
            });
        }

        JButton btnVazhdo = new RoundedButton("Vazhdo te Pagesa & Pasagjeri  >", PINK_LUX, Color.WHITE, 20);
        btnVazhdo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVazhdo.setPreferredSize(new Dimension(250, 42));
        btnVazhdo.addActionListener(e -> {
            String sel = comboHoteli.getSelectedItem() != null ? comboHoteli.getSelectedItem().toString() : "";
            double hCmim = sel.contains("145 €") ? 145 : sel.contains("165 €") ? 165 : sel.contains("85 €") ? 85 : sel.contains("92 €") ? 92 : sel.contains("52 €") ? 52 : sel.contains("45 €") ? 45 : 0;
            int nete = (int) spinNetet.getValue();
            double kostoShtese = kostoPaketa[0] + (hCmim * nete)
                    + (chkPriority.isSelected() ? 10 : 0) + (chkUshqim.isSelected() ? 12 : 0) + (chkSigurim.isSelected() ? 8 : 0);
            String paketaFinal = emriPaketes[0] + (hCmim > 0 ? " + " + sel.split("-")[0].trim() + " (" + nete + " netë)" : "");
            dritarjaPaketa.dispose();
            hapDritarenEPagesesDhePasagjerit(f, ulesja, 20.0, paketaFinal, "VIP Service", kostoShtese);
        });

        footer.add(lblTotali, BorderLayout.WEST);
        footer.add(btnVazhdo, BorderLayout.EAST);
        dritarjaPaketa.add(footer, BorderLayout.SOUTH);
        dritarjaPaketa.setVisible(true);
    }

    private void hapDritarenEPagesesDhePasagjerit(Fluturimi f, Ulesja ulesja, double pesha, String paketa, String sherbimet, double kostoShtese) {
        JDialog dlg = new JDialog(this, "COPA AIR  |  Hapi 3/3: Pasagjeri & Pagesa", true);
        dlg.setSize(620, 540);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(BLUSH_BG);

        JPanel top = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, BURGUNDY, getWidth(), 0, PINK_LUX));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        top.setBorder(new EmptyBorder(14, 22, 14, 22));
        top.add(new JLabel("<html><font color='#EBB95C' size='4'><b>💳 TË DHËNAT E PASAGJERIT & PAGESA</b></font></html>"), BorderLayout.WEST);
        dlg.add(top, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BLUSH_BG);
        body.setBorder(new EmptyBorder(16, 24, 16, 24));

        RoundedPanel formCard = new RoundedPanel(20, Color.WHITE, PINK_SOFT);
        formCard.setLayout(new GridLayout(4, 2, 10, 10));
        formCard.setBorder(new EmptyBorder(14, 16, 14, 16));

        JTextField fEmri = new JTextField();
        JTextField fMbiemri = new JTextField();
        JTextField fNrPers = new JTextField();
        JTextField fPasaporta = new JTextField("P01234567");

        formCard.add(new JLabel("👤 Emri:")); formCard.add(fEmri);
        formCard.add(new JLabel("👤 Mbiemri:")); formCard.add(fMbiemri);
        formCard.add(new JLabel("🆔 Numri Personal:")); formCard.add(fNrPers);
        formCard.add(new JLabel("🛂 Pasaporta:")); formCard.add(fPasaporta);
        body.add(formCard);
        body.add(Box.createVerticalStrut(14));

        RoundedPanel payCard = new RoundedPanel(20, Color.WHITE, PINK_LUX);
        payCard.setLayout(new GridLayout(2, 3, 8, 8));
        payCard.setBorder(new EmptyBorder(12, 16, 12, 16));
        String[] ops = {"💳 Visa / Mastercard", "🍎 Apple Pay", "🤖 Google Pay", "🅿 PayPal Express", "🏦 Bank Transfer", "👑 Pink VIP Miles"};
        ButtonGroup bgPay = new ButtonGroup();
        JRadioButton[] rbs = new JRadioButton[ops.length];
        for (int i = 0; i < ops.length; i++) {
            rbs[i] = new JRadioButton(ops[i], i == 0);
            rbs[i].setBackground(BLUSH_PINK);
            rbs[i].setFont(new Font("Segoe UI", Font.BOLD, 12));
            bgPay.add(rbs[i]);
            payCard.add(rbs[i]);
        }
        body.add(payCard);
        dlg.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(Color.WHITE);
        JButton btnPaguaj = new RoundedButton("✔ Paguaj & Konfirmo Rezervimin", PINK_LUX, Color.WHITE, 20);
        btnPaguaj.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPaguaj.setPreferredSize(new Dimension(265, 42));
        btnPaguaj.addActionListener(e -> {
            if (fEmri.getText().trim().isEmpty() || fMbiemri.getText().trim().isEmpty()) {
                shfaqMesazhModern("Kujdes", "Plotësoni emrin dhe mbiemrin e pasagjerit!");
                return;
            }
            String menyra = "Visa / Mastercard";
            for (JRadioButton rb : rbs) if (rb.isSelected()) menyra = rb.getText();
            boolean kaPromo = fushaPromoCode.getText().trim().equalsIgnoreCase("COPA20");
            Pasagjeri p = sistemi.gjejOseKrijoPasagjer(fEmri.getText().trim(), fMbiemri.getText().trim(), fNrPers.getText().trim(), fPasaporta.getText().trim());
            Bileta b = sistemi.rezervoBileteKomplete(p, f, ulesja, pesha, paketa, sherbimet, kostoShtese, menyra, kaPromo);
            if (b != null) {
                dlg.dispose();
                rifreskoMyTrips();
                shfaqFaqenEKonfirmimit(b);
            }
        });
        footer.add(btnPaguaj);
        dlg.add(footer, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void shfaqBoardingPassReal(Bileta b) {
        JDialog bp = new JDialog(this, "COPA AIR OFFICIAL BOARDING PASS - " + b.getKodiRezervimit(), true);
        bp.setSize(680, 410);
        bp.setLocationRelativeTo(this);
        bp.setLayout(new BorderLayout());
        bp.getContentPane().setBackground(BLUSH_BG);

        JPanel top = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, BURGUNDY, getWidth(), 0, PINK_LUX));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        top.setBorder(new EmptyBorder(14, 22, 14, 22));
        top.add(new JLabel("<html><font color='#EBB95C' size='5'><b>✦ COPA AIR</b></font> <font color='white' size='4'> — ELECTRONIC BOARDING PASS</font></html>"), BorderLayout.WEST);

        JPanel ticketBody = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight(), splitX = (int) (w * 0.70);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(16, 12, w - 32, h - 24, 28, 28);
                g2.setColor(PINK_SOFT);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(16, 12, w - 32, h - 24, 28, 28);

                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6}, 0));
                g2.drawLine(splitX, 18, splitX, h - 18);
            }
        };
        ticketBody.setOpaque(false);
        ticketBody.setBorder(new EmptyBorder(24, 34, 24, 34));

        JPanel majtas = new JPanel(new GridLayout(4, 2, 12, 8));
        majtas.setOpaque(false);
        majtas.add(new JLabel("<html><font color='gray' size='2'>PASSENGER</font><br><b>" + b.getPasagjeri().getEmriPlote().toUpperCase() + "</b></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>FLIGHT & TIME</font><br><b>" + b.getFluturimi().getNumriFluturimit() + " • " + b.getFluturimi().getDataOraNisjes() + "</b></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>FROM</font><br><font size='4' color='#5C0C30'><b>" + b.getFluturimi().getNisja().getQyteti() + " (" + b.getFluturimi().getNisja().getKodiIATA() + ")</b></font></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>TO</font><br><font size='4' color='#BC246C'><b>" + b.getFluturimi().getMberritja().getQyteti() + " (" + b.getFluturimi().getMberritja().getKodiIATA() + ")</b></font></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>GATE</font><br><font size='5' color='#5C0C30'><b>" + b.getFluturimi().getPortaNisjes() + "</b></font></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>SEAT</font><br><font size='5' color='#BC246C'><b>" + b.getUlesja().getNumriUleses() + "</b></font></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>PACKAGE</font><br><b>" + b.getPaketa() + "</b></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>PAID</font><br><b>" + String.format("%.2f", b.getPagesa().getShumaTotale()) + " EUR</b></html>"));

        JPanel djathtas = new JPanel();
        djathtas.setOpaque(false);
        djathtas.setPreferredSize(new Dimension(165, 220));
        djathtas.setLayout(new BoxLayout(djathtas, BoxLayout.Y_AXIS));

        String pnrPastër = b.getKodiRezervimit().trim();
        JLabel lblPnr = new JLabel(pnrPastër);
        lblPnr.setFont(new Font("Consolas", Font.BOLD, 24));
        lblPnr.setForeground(BURGUNDY);
        lblPnr.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCopyPNR = new RoundedButton("📋 Copy PNR Code", PINK_LUX, Color.WHITE, 16);
        btnCopyPNR.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnCopyPNR.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCopyPNR.addActionListener(e -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(pnrPastër), null);
            btnCopyPNR.setText("✔ Copied: " + pnrPastër);
        });

        djathtas.add(Box.createVerticalStrut(30));
        djathtas.add(lblPnr);
        djathtas.add(Box.createVerticalStrut(20));
        djathtas.add(btnCopyPNR);

        ticketBody.add(majtas, BorderLayout.CENTER);
        ticketBody.add(djathtas, BorderLayout.EAST);

        bp.add(top, BorderLayout.NORTH);
        bp.add(ticketBody, BorderLayout.CENTER);
        bp.setVisible(true);
    }

    // ================= NDIHMËS DHE KLASA GRAFIKE =================
    private void shfaqMesazhModern(String titulli, String mesazhi) {
        JDialog d = new JDialog(this, titulli, true);
        d.setSize(450, 220);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(Color.WHITE);

        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(BURGUNDY);
        head.setBorder(new EmptyBorder(12, 18, 12, 18));
        JLabel l = new JLabel("✦ " + titulli);
        l.setForeground(CHAMPAGNE_GOLD);
        l.setFont(new Font("Segoe UI", Font.BOLD, 15));
        head.add(l, BorderLayout.WEST);

        JTextArea txt = new JTextArea(mesazhi);
        txt.setEditable(false); txt.setLineWrap(true); txt.setWrapStyleWord(true);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(new EmptyBorder(16, 20, 10, 20));

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        foot.setBackground(Color.WHITE);
        JButton ok = new RoundedButton("OK", PINK_LUX, Color.WHITE, 16);
        ok.addActionListener(e -> d.dispose());
        foot.add(ok);

        d.add(head, BorderLayout.NORTH); d.add(txt, BorderLayout.CENTER); d.add(foot, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private String shfaqInputModern(String titulli, String pyetja) {
        final String[] res = {null};
        JDialog d = new JDialog(this, titulli, true);
        d.setSize(430, 210);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(Color.WHITE);

        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(BURGUNDY);
        head.setBorder(new EmptyBorder(12, 18, 12, 18));
        JLabel l = new JLabel("✦ " + titulli);
        l.setForeground(CHAMPAGNE_GOLD);
        head.add(l, BorderLayout.WEST);

        JPanel c = new JPanel(new GridLayout(2, 1, 6, 6));
        c.setBackground(Color.WHITE);
        c.setBorder(new EmptyBorder(14, 20, 8, 20));
        JTextField tf = new JTextField();
        c.add(new JLabel(pyetja)); c.add(tf);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        foot.setBackground(Color.WHITE);
        JButton ok = new RoundedButton("Vazhdo", PINK_LUX, Color.WHITE, 16);
        ok.addActionListener(e -> { res[0] = tf.getText(); d.dispose(); });
        foot.add(ok);

        d.add(head, BorderLayout.NORTH); d.add(c, BorderLayout.CENTER); d.add(foot, BorderLayout.SOUTH);
        d.setVisible(true);
        return res[0];
    }

    private void hapDritarenShtoFluturim() {
        JDialog d = new JDialog(this, "Shto Fluturim të Ri", true);
        d.setSize(460, 410);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(new EmptyBorder(16, 20, 16, 20));
        JTextField fNr = new JTextField("CM-909");
        JComboBox<Aeroporti> cNisja = new JComboBox<>(sistemi.getAeroportet().toArray(new Aeroporti[0]));
        JComboBox<Aeroporti> cMberritja = new JComboBox<>(sistemi.getAeroportet().toArray(new Aeroporti[0]));
        cMberritja.setSelectedIndex(4);
        JTextField fData = new JTextField("2026-12-25 | 10:00 → 12:15 (2h 15m)");
        JTextField fCmimi = new JTextField("89");
        JTextField fPorta = new JTextField("Gate A5");

        form.add(new JLabel("Kodi:")); form.add(fNr);
        form.add(new JLabel("Origin:")); form.add(cNisja);
        form.add(new JLabel("Destination:")); form.add(cMberritja);
        form.add(new JLabel("Orari:")); form.add(fData);
        form.add(new JLabel("Çmimi (EUR):")); form.add(fCmimi);
        form.add(new JLabel("Gate:")); form.add(fPorta);

        JButton btnRuaj = new RoundedButton("✔ Shto Fluturimin", PINK_LUX, Color.WHITE, 18);
        btnRuaj.addActionListener(e -> {
            try {
                sistemi.shtoFluturimTeRi(fNr.getText().trim(), (Aeroporti) cNisja.getSelectedItem(),
                        (Aeroporti) cMberritja.getSelectedItem(), fData.getText().trim(),
                        sistemi.getFlota().get(0), sistemi.getPilotet().get(0),
                        Double.parseDouble(fCmimi.getText().trim()), fPorta.getText().trim());
                d.dispose();
                shfaqMesazhModern("Sukses", "Fluturimi u shtua!");
            } catch (Exception ex) {
                shfaqMesazhModern("Gabim", "Çmimi duhet të jetë numër!");
            }
        });
        d.add(form, BorderLayout.CENTER);
        d.add(btnRuaj, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private RoundedPanel krijoKarteleTarife(String titulli, String cmimi, String detajet, boolean zgjedhur) {
        RoundedPanel card = new RoundedPanel(20, zgjedhur ? BLUSH_PINK : Color.WHITE, zgjedhur ? PINK_LUX : new Color(235, 195, 215));
        card.setLayout(new BorderLayout(6, 8));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(new EmptyBorder(10, 14, 10, 14));
        card.add(new JLabel("<html><font color='#5C0C30'><b>" + titulli + "</b></font><br><font color='#BC246C' size='5'><b>" + cmimi + "</b></font></html>"), BorderLayout.NORTH);
        JTextArea txt = new JTextArea(detajet);
        txt.setEditable(false); txt.setOpaque(false);
        card.add(txt, BorderLayout.CENTER);
        return card;
    }

    private JCheckBox stilezoAddon(String emri, String cmimi) {
        JCheckBox chk = new JCheckBox("<html><b>" + emri + "</b> <font color='#BC246C'>" + cmimi + "</font></html>");
        chk.setBackground(Color.WHITE);
        return chk;
    }

    private JButton krijoLinkNav(String teksti) {
        JButton btn = new JButton(teksti);
        btn.setForeground(new Color(255, 228, 240));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setForeground(CHAMPAGNE_GOLD); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setForeground(new Color(255, 228, 240)); }
        });
        navButonat.add(btn);
        return btn;
    }

    static class RoundedPanel extends JPanel {
        private int radius;
        private Color borderColor;
        public RoundedPanel(int radius, Color bgColor, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
            setBackground(bgColor);
            setOpaque(false);
        }
        public void setBorderColor(Color c) { this.borderColor = c; repaint(); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 3, getHeight() - 3, radius, radius));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedButton extends JButton {
        private int radius;
        public RoundedButton(String text, Color bg, Color fg, int radius) {
            super(text);
            this.radius = radius;
            setBackground(bg);
            setForeground(fg);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isEnabled() ? getBackground() : Color.LIGHT_GRAY);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NderfaqjaGrafike().setVisible(true));
    }
}
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Comparator;

public class NderfaqjaGrafike extends JFrame {
    private SistemiRezervimeve sistemi;
    private CardLayout cardLayout;
    private JPanel qendraKardave;

    // Kontrollet e kërkimit në faqen Welcome
    private JComboBox<String> filtroNisja;
    private JComboBox<String> filtroMberritja;
    private JComboBox<String> filtroData;
    private JComboBox<String> filtroKlasa;
    private JTextField fushaPromoCode;
    private JRadioButton rbOneWay;
    private JRadioButton rbReturn;
    private JCheckBox chkVetemTeLirat;

    // Panelët e kartelave
    private JPanel listaKartelaveFluturimeve;
    private JLabel lblTitulliRezultateve;
    private JPanel listaKartelaveBiletave;
    private String nisjaFundit = "Të gjitha";
    private String mberritjaFundit = "Të gjitha";
    private boolean renditNgaCmimi = false;

    // Paleta Zyrtare PINK OMBRÉ EDITION
    private final Color ROSE_DARK = new Color(102, 16, 66);        // #661042
    private final Color PINK_DEEP = new Color(184, 37, 111);       // #B8256F
    private final Color PINK_MID = new Color(230, 88, 152);        // #E65898
    private final Color PINK_SOFT = new Color(255, 232, 243);      // #FFE8F3
    private final Color PINK_BG = new Color(253, 245, 249);        // #FDF5F9
    private final Color ROSE_GOLD = new Color(245, 194, 107);      // #F5C26B
    private final Color E_KUQE_ZENE = new Color(214, 48, 75);

    public NderfaqjaGrafike() {
        sistemi = new SistemiRezervimeve();

        setTitle("COPA AIR  |  Pink Ombré Luxury Edition — 33 Destinations, 30-Row Cabin & 5★ Hotels");
        setSize(1200, 775);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(PINK_BG);

        // ================= 1. TOP NAVIGATION BAR (PINK OMBRÉ) =================
        JPanel navBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, ROSE_DARK, getWidth(), 0, PINK_DEEP);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        navBar.setBorder(new EmptyBorder(12, 28, 12, 28));

        JPanel navMajtas = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 2));
        navMajtas.setOpaque(false);

        JButton logoCopa = new JButton("<html><font color='#F5C26B' size='5'><b>🌸 COPA</b></font> <font color='white' size='5'><b>AIR</b></font></html>");
        logoCopa.setBorderPainted(false);
        logoCopa.setContentAreaFilled(false);
        logoCopa.setFocusPainted(false);
        logoCopa.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnNavHome = krijoLinkNav("🏠 Welcome / Search");
        JButton btnNavFlights = krijoLinkNav("✈ 33 Destinations");
        JButton btnNavCheap = krijoLinkNav("💸 Cheapest Flights");
        JButton btnNavMyTrips = krijoLinkNav("🎫 My Trips & Printo Biletën");
        JButton btnNavCheckIn = krijoLinkNav("📱 Online Check-in");
        JButton btnNavAdmin = krijoLinkNav("⚙ Admin");

        navMajtas.add(logoCopa);
        navMajtas.add(btnNavHome);
        navMajtas.add(btnNavFlights);
        navMajtas.add(btnNavCheap);
        navMajtas.add(btnNavMyTrips);
        navMajtas.add(btnNavCheckIn);
        navMajtas.add(btnNavAdmin);

        JButton btnConnectMiles = new RoundedButton("★ Pink VIP Club", new Color(135, 22, 85), ROSE_GOLD, 20);
        btnConnectMiles.setFont(new Font("Segoe UI", Font.BOLD, 12));

        navBar.add(navMajtas, BorderLayout.WEST);
        navBar.add(btnConnectMiles, BorderLayout.EAST);
        add(navBar, BorderLayout.NORTH);

        // ================= 2. FAQET ME CARDLAYOUT =================
        cardLayout = new CardLayout();
        qendraKardave = new JPanel(cardLayout);

        qendraKardave.add(krijoFaqenWelcome(), "WELCOME");
        qendraKardave.add(krijoFaqenRezultateve(), "FLIGHTS");
        qendraKardave.add(krijoFaqenMyTrips(), "MY_TRIPS");
        qendraKardave.add(krijoFaqenAdmin(), "ADMIN");

        add(qendraKardave, BorderLayout.CENTER);

        // Navigimi
        logoCopa.addActionListener(e -> cardLayout.show(qendraKardave, "WELCOME"));
        btnNavHome.addActionListener(e -> cardLayout.show(qendraKardave, "WELCOME"));
        btnNavFlights.addActionListener(e -> {
            renditNgaCmimi = false;
            shfaqKartelatEFluturimeve("Të gjitha", "Të gjitha", false);
            cardLayout.show(qendraKardave, "FLIGHTS");
        });
        btnNavCheap.addActionListener(e -> {
            renditNgaCmimi = true;
            shfaqKartelatEFluturimeve("Të gjitha", "Të gjitha", true);
            cardLayout.show(qendraKardave, "FLIGHTS");
        });
        btnNavMyTrips.addActionListener(e -> {
            rifreskoMyTrips();
            cardLayout.show(qendraKardave, "MY_TRIPS");
        });
        btnNavCheckIn.addActionListener(e -> hapOnlineCheckIn());
        btnNavAdmin.addActionListener(e -> cardLayout.show(qendraKardave, "ADMIN"));
        btnConnectMiles.addActionListener(e -> shfaqMesazhModern("Pink VIP Club & Promo",
            "• Fitoni pikë me çdo fluturim dhe hotel 5★ të rezervuar!\n• Përdorni kodin promocional 'COPA20' në kërkim për 10% zbritje të menjëhershme."));
    }

    // ================= FAQJA 1: WELCOME PAGE (PINK OMBRÉ + ROUNDED CARDS) =================
    private JPanel krijoFaqenWelcome() {
        JPanel welcomePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Ombré i bukur rozë (nga Dark Plum Rose në Pastel Rose)
                GradientPaint bgGrad = new GradientPaint(0, 0, ROSE_DARK, w, h, new Color(248, 165, 202));
                g2.setPaint(bgGrad);
                g2.fillRect(0, 0, w, h);

                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillOval(w - 480, -80, 620, 620);
                g2.fillOval(-120, h - 320, 440, 440);

                g2.setColor(new Color(245, 194, 107, 150));
                g2.setStroke(new BasicStroke(3.5f));
                g2.drawArc(w - 520, 40, 560, 320, 30, 145);
            }
        };
        welcomePanel.setBorder(new EmptyBorder(34, 60, 34, 60));

        JPanel heroText = new JPanel();
        heroText.setOpaque(false);
        heroText.setLayout(new BoxLayout(heroText, BoxLayout.Y_AXIS));

        JLabel badge = new JLabel("  🌸  COPA AIR PINK OMBRÉ EDITION  •  33 DESTINATIONS & 5★ HOTELS  ");
        badge.setOpaque(true);
        badge.setBackground(new Color(255, 255, 255, 45));
        badge.setForeground(ROSE_GOLD);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel titulli = new JLabel("Where would you like to fly today?");
        titulli.setFont(new Font("Segoe UI", Font.BOLD, 38));
        titulli.setForeground(Color.WHITE);

        JLabel nentitulli = new JLabel("Zgjidhni nga 33 destinacione botërore, gjeni çmimet më të lira dhe rezervoni hotelin me deri në 5 yje!");
        nentitulli.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nentitulli.setForeground(new Color(255, 236, 245));

        heroText.add(badge);
        heroText.add(Box.createVerticalStrut(10));
        heroText.add(titulli);
        heroText.add(Box.createVerticalStrut(6));
        heroText.add(nentitulli);

        welcomePanel.add(heroText, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        // Karta e kërkimit me kënde të rrumbullakosura (RoundedPanel)
        RoundedPanel searchCard = new RoundedPanel(30, Color.WHITE, ROSE_GOLD);
        searchCard.setLayout(new BorderLayout(15, 14));
        searchCard.setPreferredSize(new Dimension(1040, 225));
        searchCard.setBorder(new EmptyBorder(20, 26, 20, 26));

        JPanel rreshtiSiperm = new JPanel(new BorderLayout());
        rreshtiSiperm.setOpaque(false);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        radioPanel.setOpaque(false);
        rbOneWay = new JRadioButton("One-way (Një drejtim)", true);
        rbReturn = new JRadioButton("Round-trip (Vajtje-Ardhje)");
        chkVetemTeLirat = new JCheckBox("💸 Rendit nga më të lirat (Cheapest First)");
        rbOneWay.setOpaque(false);
        rbReturn.setOpaque(false);
        chkVetemTeLirat.setOpaque(false);
        rbOneWay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        rbReturn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        chkVetemTeLirat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        rbOneWay.setForeground(PINK_DEEP);
        chkVetemTeLirat.setForeground(ROSE_DARK);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbOneWay);
        bg.add(rbReturn);

        radioPanel.add(rbOneWay);
        radioPanel.add(rbReturn);
        radioPanel.add(chkVetemTeLirat);

        JPanel promoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        promoPanel.setOpaque(false);
        JLabel lblPromo = new JLabel("🏷 Promo (COPA20):");
        lblPromo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPromo.setForeground(ROSE_DARK);
        fushaPromoCode = new JTextField(9);
        fushaPromoCode.setFont(new Font("Segoe UI", Font.BOLD, 13));
        promoPanel.add(lblPromo);
        promoPanel.add(fushaPromoCode);

        rreshtiSiperm.add(radioPanel, BorderLayout.WEST);
        rreshtiSiperm.add(promoPanel, BorderLayout.EAST);

        JPanel gridZgjedhjeve = new JPanel(new GridLayout(1, 4, 14, 0));
        gridZgjedhjeve.setOpaque(false);

        filtroNisja = new JComboBox<>();
        filtroMberritja = new JComboBox<>();
        filtroNisja.addItem("Të gjitha nisjet (Origin)");
        filtroMberritja.addItem("Të gjitha destinacionet (To)");
        for (Aeroporti a : sistemi.getAeroportet()) {
            filtroNisja.addItem(a.getKodiIATA() + " - " + a.getQyteti());
            filtroMberritja.addItem(a.getKodiIATA() + " - " + a.getQyteti());
        }

        filtroData = new JComboBox<>(new String[]{
            "Çdo datë (Dhjetor 2026)",
            "2026-12-15 (E Martë)",
            "2026-12-16 (E Mërkurë)",
            "2026-12-17 (E Enjte)",
            "2026-12-18 (E Premte)",
            "2026-12-20 (E Diel)"
        });

        filtroKlasa = new JComboBox<>(new String[]{
            "Economy & Business",
            "Economy Class",
            "Pink Luxury Business"
        });

        gridZgjedhjeve.add(krijoKutiInputi("🛫 ORIGIN (NGA)", filtroNisja));
        gridZgjedhjeve.add(krijoKutiInputi("🛬 DESTINATION (NË)", filtroMberritja));
        gridZgjedhjeve.add(krijoKutiInputi("🕒 DATA & ORA", filtroData));
        gridZgjedhjeve.add(krijoKutiInputi("💺 KLASA", filtroKlasa));

        JPanel rreshtiPoshtem = new JPanel(new BorderLayout());
        rreshtiPoshtem.setOpaque(false);

        JButton btnShfaqTeGjitha = new JButton("Shfaq të gjitha " + sistemi.getFluturimet().size() + " fluturimet  →");
        btnShfaqTeGjitha.setBorderPainted(false);
        btnShfaqTeGjitha.setContentAreaFilled(false);
        btnShfaqTeGjitha.setForeground(PINK_DEEP);
        btnShfaqTeGjitha.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnShfaqTeGjitha.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnSearchFlights = new RoundedButton("🔍  KËRKO FLUTURIMET  >", PINK_DEEP, Color.WHITE, 24);
        btnSearchFlights.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSearchFlights.setPreferredSize(new Dimension(265, 44));

        rreshtiPoshtem.add(btnShfaqTeGjitha, BorderLayout.WEST);
        rreshtiPoshtem.add(btnSearchFlights, BorderLayout.EAST);

        searchCard.add(rreshtiSiperm, BorderLayout.NORTH);
        searchCard.add(gridZgjedhjeve, BorderLayout.CENTER);
        searchCard.add(rreshtiPoshtem, BorderLayout.SOUTH);

        centerWrapper.add(searchCard);
        welcomePanel.add(centerWrapper, BorderLayout.CENTER);

        // Kartat e destinacioneve më të lira (Rounded)
        JPanel popularPanel = new JPanel(new BorderLayout(0, 10));
        popularPanel.setOpaque(false);

        JLabel lblPop = new JLabel("🌸 OFERTAT MË TË LIRA (CHEAPEST DEALS — Kliko për oraret dhe hotelin):");
        lblPop.setForeground(Color.WHITE);
        lblPop.setFont(new Font("Segoe UI", Font.BOLD, 14));
        popularPanel.add(lblPop, BorderLayout.NORTH);

        JPanel cardsDest = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsDest.setOpaque(false);

        cardsDest.add(krijoKarteleDestinacioni("Tiranë (TIA)", "Dubrovnik (DBV)", "Nga 44 EUR • 09:30", "TIA", "DBV"));
        cardsDest.add(krijoKarteleDestinacioni("Tiranë (TIA)", "Milano (MXP)", "Nga 45 EUR • 10:15", "TIA", "MXP"));
        cardsDest.add(krijoKarteleDestinacioni("Prishtinë (PRN)", "Milano (MXP)", "Nga 54 EUR • 17:40", "PRN", "MXP"));
        cardsDest.add(krijoKarteleDestinacioni("Prishtinë (PRN)", "Vjenë (VIE)", "Nga 59 EUR • 09:45", "PRN", "VIE"));

        popularPanel.add(cardsDest, BorderLayout.CENTER);
        welcomePanel.add(popularPanel, BorderLayout.SOUTH);

        btnSearchFlights.addActionListener(e -> {
            renditNgaCmimi = chkVetemTeLirat.isSelected();
            shfaqKartelatEFluturimeve((String) filtroNisja.getSelectedItem(), (String) filtroMberritja.getSelectedItem(), renditNgaCmimi);
            cardLayout.show(qendraKardave, "FLIGHTS");
        });
        btnShfaqTeGjitha.addActionListener(e -> {
            renditNgaCmimi = chkVetemTeLirat.isSelected();
            shfaqKartelatEFluturimeve("Të gjitha", "Të gjitha", renditNgaCmimi);
            cardLayout.show(qendraKardave, "FLIGHTS");
        });

        return welcomePanel;
    }

    private JPanel krijoKutiInputi(String etiketa, JComboBox<String> combo) {
        RoundedPanel p = new RoundedPanel(18, PINK_SOFT, new Color(240, 195, 218));
        p.setLayout(new BorderLayout(4, 4));
        p.setBorder(new EmptyBorder(8, 12, 8, 12));
        JLabel lbl = new JLabel(etiketa);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(ROSE_DARK);
        combo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        combo.setBackground(Color.WHITE);
        p.add(lbl, BorderLayout.NORTH);
        p.add(combo, BorderLayout.CENTER);
        return p;
    }

    private JButton krijoKarteleDestinacioni(String nga, String ne, String cmimi, String kodiNga, String kodiNe) {
        RoundedButton btn = new RoundedButton("<html><div style='padding:4px;'>"
                + "<font color='#F5C26B' size='3'><b>🌸 CHEAPEST DEAL</b></font><br>"
                + "<font color='white' size='4'><b>" + nga + " ➔ " + ne + "</b></font><br>"
                + "<font color='#FFE8F3' size='3'>" + cmimi + "</font></div></html>",
                new Color(138, 22, 82), Color.WHITE, 22);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 14, 10, 14));
        btn.addActionListener(e -> {
            shfaqKartelatEFluturimeve(kodiNga, kodiNe, true);
            cardLayout.show(qendraKardave, "FLIGHTS");
        });
        return btn;
    }

    // ================= FAQJA 2: FLIGHT CARDS ME ORARE & FILTRIM CHEAPEST =================
    private JPanel krijoFaqenRezultateve() {
        JPanel paneli = new JPanel(new BorderLayout(15, 15));
        paneli.setBackground(PINK_BG);
        paneli.setBorder(new EmptyBorder(20, 45, 20, 45));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JButton btnKthehu = new RoundedButton("← Ndrysho Kërkimin", ROSE_DARK, Color.WHITE, 18);
        btnKthehu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnKthehu.addActionListener(e -> cardLayout.show(qendraKardave, "WELCOME"));

        lblTitulliRezultateve = new JLabel("Fluturimet e Disponueshme", SwingConstants.CENTER);
        lblTitulliRezultateve.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulliRezultateve.setForeground(ROSE_DARK);

        JPanel butonaDjathtas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        butonaDjathtas.setOpaque(false);

        JButton btnSortCheap = new RoundedButton("💸 Më të Lirat (Cheapest)", PINK_DEEP, Color.WHITE, 18);
        btnSortCheap.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSortCheap.addActionListener(e -> shfaqKartelatEFluturimeve(nisjaFundit, mberritjaFundit, true));

        JButton btnShfaqKrejt = new RoundedButton("Të gjitha (33)", new Color(165, 55, 115), Color.WHITE, 18);
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
        listaKartelaveFluturimeve.setBackground(PINK_BG);

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
        RoundedPanel card = new RoundedPanel(24, Color.WHITE, badgeCheap ? PINK_DEEP : new Color(235, 195, 215));
        card.setLayout(new BorderLayout(20, 0));
        card.setMaximumSize(new Dimension(1080, 110));
        card.setPreferredSize(new Dimension(1080, 110));
        card.setBorder(new EmptyBorder(14, 22, 14, 22));

        JPanel majtas = new JPanel();
        majtas.setOpaque(false);
        majtas.setLayout(new BoxLayout(majtas, BoxLayout.Y_AXIS));

        String tag = badgeCheap ? "  🔥 SUPER CHEAP" : "";
        JLabel lblKodi = new JLabel("✈ " + f.getNumriFluturimit() + " • " + f.getPortaNisjes() + tag);
        lblKodi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblKodi.setForeground(PINK_DEEP);

        JLabel lblAvioni = new JLabel(f.getAvioni().getModeli());
        lblAvioni.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAvioni.setForeground(Color.GRAY);

        // Koha e saktë e nisjes dhe mbërritjes
        JLabel lblData = new JLabel("🕒 " + f.getDataOraNisjes());
        lblData.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblData.setForeground(ROSE_DARK);

        majtas.add(lblKodi);
        majtas.add(Box.createVerticalStrut(4));
        majtas.add(lblAvioni);
        majtas.add(Box.createVerticalStrut(4));
        majtas.add(lblData);

        JPanel qendra = new JPanel(new GridLayout(1, 3, 10, 0));
        qendra.setOpaque(false);

        JLabel lblOrigin = new JLabel("<html><center><font size='5' color='#661042'><b>" + f.getNisja().getKodiIATA() + "</b></font><br>"
                + "<font color='gray'>" + f.getNisja().getQyteti() + "</font></center></html>", SwingConstants.CENTER);

        int liraEcon = f.numeroUlesetELira("Economy");
        int liraBus = f.numeroUlesetELira("Business");
        JLabel lblVija = new JLabel("<html><center><font color='#E65898'><b>━━━━━ 🌸 ━━━━━</b></font><br>"
                + "<font size='2' color='#B8256F'>Direct • " + (liraEcon + liraBus) + " ulëse të lira</font></center></html>", SwingConstants.CENTER);

        JLabel lblDest = new JLabel("<html><center><font size='5' color='#661042'><b>" + f.getMberritja().getKodiIATA() + "</b></font><br>"
                + "<font color='gray'>" + f.getMberritja().getQyteti() + "</font></center></html>", SwingConstants.CENTER);

        qendra.add(lblOrigin);
        qendra.add(lblVija);
        qendra.add(lblDest);

        JPanel djathtas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        djathtas.setOpaque(false);

        JLabel lblCmimi = new JLabel("<html><right><font size='2' color='gray'>Nga</font><br>"
                + "<font size='5' color='#661042'><b>" + String.format("%.2f", f.getCmimiDinamik()) + " €</b></font></right></html>");

        JButton btnZgjidh = new RoundedButton("Zgjidh Ulësen  >", PINK_DEEP, Color.WHITE, 20);
        btnZgjidh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnZgjidh.setPreferredSize(new Dimension(155, 42));
        btnZgjidh.addActionListener(e -> hapKabineneAvionit(f));

        djathtas.add(lblCmimi);
        djathtas.add(btnZgjidh);

        card.add(majtas, BorderLayout.WEST);
        card.add(qendra, BorderLayout.CENTER);
        card.add(djathtas, BorderLayout.EAST);
        return card;
    }

    // ================= FAQJA 3: MY TRIPS & REAL BOARDING PASS =================
    private JPanel krijoFaqenMyTrips() {
        JPanel p = new JPanel(new BorderLayout(12, 12));
        p.setBackground(PINK_BG);
        p.setBorder(new EmptyBorder(20, 45, 20, 45));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel tit = new JLabel("🎫 Rezervimet e Mia (My Trips, 5★ Hotels & Real Boarding Passes)");
        tit.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tit.setForeground(ROSE_DARK);

        JPanel butonaHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        butonaHeader.setOpaque(false);

        JButton btnPrintoFundit = new RoundedButton("🖨 Shiko / Printo Biletën e Fundit", PINK_DEEP, Color.WHITE, 18);
        btnPrintoFundit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPrintoFundit.addActionListener(e -> printoBiletenEFundit());

        JButton btnAnulo = new RoundedButton("❌ Anulo Biletë me PNR", E_KUQE_ZENE, Color.WHITE, 18);
        btnAnulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAnulo.addActionListener(e -> hapDritarenAnulimit());

        butonaHeader.add(btnPrintoFundit);
        butonaHeader.add(btnAnulo);

        header.add(tit, BorderLayout.WEST);
        header.add(butonaHeader, BorderLayout.EAST);
        p.add(header, BorderLayout.NORTH);

        listaKartelaveBiletave = new JPanel();
        listaKartelaveBiletave.setLayout(new BoxLayout(listaKartelaveBiletave, BoxLayout.Y_AXIS));
        listaKartelaveBiletave.setBackground(PINK_BG);

        JScrollPane sc = new JScrollPane(listaKartelaveBiletave);
        sc.setBorder(null);
        p.add(sc, BorderLayout.CENTER);
        return p;
    }

    private void printoBiletenEFundit() {
        Bileta eFundit = sistemi.merrBiletenEFundit();
        if (eFundit == null) {
            shfaqMesazhModern("Nuk ka bileta", "Ende nuk keni rezervuar asnjë biletë!\nShkoni te 'Welcome / Search', zgjidhni një fluturim dhe konfirmoni rezervimin.");
            return;
        }
        eFundit.kryejCheckIn();
        shfaqBoardingPassReal(eFundit);
        rifreskoMyTrips();
    }

    private void rifreskoMyTrips() {
        if (listaKartelaveBiletave == null) return;
        listaKartelaveBiletave.removeAll();
        int nr = 0;
        for (Bileta b : sistemi.getTeGjithaBiletat()) {
            if (b.isEshteAktiv()) {
                nr++;
                RoundedPanel c = new RoundedPanel(22, Color.WHITE, PINK_MID);
                c.setLayout(new BorderLayout(15, 0));
                c.setMaximumSize(new Dimension(1080, 95));
                c.setBorder(new EmptyBorder(12, 20, 12, 20));

                JLabel info = new JLabel("<html><font color='#661042' size='4'><b>PNR: " + b.getKodiRezervimit() + "</b></font>  |  "
                        + "<b>" + b.getFluturimi().getNumriFluturimit() + "</b> (" + b.getFluturimi().getNisja().getKodiIATA() + " ➔ " + b.getFluturimi().getMberritja().getKodiIATA() + ") • " + b.getFluturimi().getDataOraNisjes() + "<br>"
                        + "Pasagjeri: <b>" + b.getPasagjeri().getEmriPlote() + "</b>  •  Ulësja: <b>" + b.getUlesja().getNumriUleses() + "</b>  •  Paketa & Hoteli: <b>" + b.getPagesa() + "</b>  •  Paguar: <b>" + String.format("%.2f", b.getPagesa().getShumaTotale()) + " EUR</b></html>");

                JButton btnBP = new RoundedButton("🎫 Shiko Boarding Pass & Copy PNR", PINK_DEEP, Color.WHITE, 18);
                btnBP.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnBP.addActionListener(e -> {
                    b.kryejCheckIn();
                    shfaqBoardingPassReal(b);
                    rifreskoMyTrips();
                });

                c.add(info, BorderLayout.CENTER);
                c.add(btnBP, BorderLayout.EAST);
                listaKartelaveBiletave.add(c);
                listaKartelaveBiletave.add(Box.createVerticalStrut(10));
            }
        }
        if (nr == 0) {
            JLabel bosh = new JLabel("Ende nuk keni rezervuar asnjë biletë. Kthehuni te 'Welcome / Search' për të rezervuar!", SwingConstants.CENTER);
            bosh.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            listaKartelaveBiletave.add(bosh);
        }
        listaKartelaveBiletave.revalidate();
        listaKartelaveBiletave.repaint();
    }

    // ================= FAQJA 4: ADMIN =================
    private JPanel krijoFaqenAdmin() {
        JPanel p = new JPanel(new BorderLayout(12, 12));
        p.setBackground(PINK_BG);
        p.setBorder(new EmptyBorder(20, 45, 20, 45));

        RoundedPanel paneliKontrollit = new RoundedPanel(20, Color.WHITE, PINK_MID);
        paneliKontrollit.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JButton btnShtoFluturim = new RoundedButton("+ Shto Fluturim të Ri", PINK_DEEP, Color.WHITE, 18);
        JButton btnManifesti = new RoundedButton("📋 Manifesti i Pasagjerëve", ROSE_DARK, Color.WHITE, 18);
        JButton btnFitimet = new RoundedButton("💰 Fitimet Totale", new Color(195, 130, 35), Color.WHITE, 18);
        JButton btnEksporto = new RoundedButton("📄 Eksporto Raportin (.txt)", new Color(40, 140, 85), Color.WHITE, 18);

        paneliKontrollit.add(btnShtoFluturim);
        paneliKontrollit.add(btnManifesti);
        paneliKontrollit.add(btnFitimet);
        paneliKontrollit.add(btnEksporto);

        JTextArea zonaAdmin = new JTextArea();
        zonaAdmin.setEditable(false);
        zonaAdmin.setFont(new Font("Consolas", Font.PLAIN, 13));
        zonaAdmin.setText("Copa Air Pink Ombré Operations:\n- 33 Destinacione aktive • Kontrolloni pasagjerët dhe raportet.");

        btnShtoFluturim.addActionListener(e -> hapDritarenShtoFluturim());
        btnFitimet.addActionListener(e -> {
            double totali = sistemi.llogaritTeArdhuratTotale();
            zonaAdmin.setText("================ RAPORTI FINANCIAR COPA AIR ================\n"
                    + "Fluturime aktive: " + sistemi.getFluturimet().size() + "\n"
                    + "Të ardhurat totale (Fluturime + Hotele 5★): " + String.format("%.2f", totali) + " EUR\n"
                    + "============================================================");
        });
        btnEksporto.addActionListener(e -> {
            sistemi.eksportoRaportin();
            shfaqMesazhModern("Raporti u ruajt", "Raporti financiar u ruajt me sukses në 'raporti_kompanise.txt'!");
        });
        btnManifesti.addActionListener(e -> {
            String nr = shfaqInputModern("Manifesti i Pasagjerëve", "Shkruani kodin e fluturimit (p.sh. CM-101):");
            if (nr != null && !nr.trim().isEmpty()) {
                Fluturimi f = sistemi.gjejFluturim(nr.trim());
                if (f == null) {
                    shfaqMesazhModern("Gabim", "Fluturimi '" + nr + "' nuk u gjet!");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("=== MANIFESTI: ").append(f.getNumriFluturimit()).append(" ===\n\n");
                int nrPas = 0;
                for (Bileta b : sistemi.getTeGjithaBiletat()) {
                    if (b.isEshteAktiv() && b.getFluturimi().equals(f)) {
                        nrPas++;
                        sb.append(nrPas).append(". ").append(b.getPasagjeri().getEmriPlote())
                          .append(" | PNR: ").append(b.getKodiRezervimit())
                          .append(" | Ulësja: ").append(b.getUlesja().getNumriUleses())
                          .append(" | Paketa: ").append(b.getPagesa()).append("\n");
                    }
                }
                if (nrPas == 0) sb.append("Ende nuk ka pasagjerë në këtë fluturim.");
                zonaAdmin.setText(sb.toString());
            }
        });

        p.add(paneliKontrollit, BorderLayout.NORTH);
        p.add(new JScrollPane(zonaAdmin), BorderLayout.CENTER);
        return p;
    }

    // ================= KABINA VIZUALE 30 RRESHTA (PINK OMBRÉ INTERIOR) =================
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
                g2.setPaint(new GradientPaint(0, 0, ROSE_DARK, getWidth(), 0, PINK_DEEP));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        koka.setBorder(new EmptyBorder(10, 18, 10, 18));
        JLabel info = new JLabel("<html><center><font color='white' size='4'><b>🌸 KABINA PINK OMBRÉ (" + f.getAvioni().getModeli() + ")  •  30 RRESHTA (1A–30F)</b></font><br>"
                + "<font color='#F5C26B'>■ Business Class (Rreshtat 1–3)</font> &nbsp;&nbsp; "
                + "<font color='#FF99CC'>■ Extra Legroom / Exit Row (12–13, 30)</font> &nbsp;&nbsp; "
                + "<font color='#FFE8F3'>■ Economy Class</font> &nbsp;&nbsp; "
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
        lblTitulliInspector.setForeground(ROSE_DARK);

        JLabel lblNumriUleses = new JLabel("Asnjë ulëse e zgjedhur");
        lblNumriUleses.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNumriUleses.setForeground(PINK_DEEP);

        JLabel lblPozicioni = new JLabel("Pozicioni: —");
        lblPozicioni.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblKategoria = new JLabel("Klasa: —");
        lblKategoria.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblCmimiUleses = new JLabel("Çmimi total: " + String.format("%.2f", f.getCmimiDinamik()) + " EUR");
        lblCmimiUleses.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblCmimiUleses.setForeground(new Color(34, 153, 84));

        JTextArea pershkrimiUleses = new JTextArea(6, 20);
        pershkrimiUleses.setEditable(false);
        pershkrimiUleses.setLineWrap(true);
        pershkrimiUleses.setWrapStyleWord(true);
        pershkrimiUleses.setBackground(PINK_SOFT);
        pershkrimiUleses.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pershkrimiUleses.setBorder(new EmptyBorder(8, 8, 8, 8));
        pershkrimiUleses.setText("Lëvizni poshtë (scroll) për t'i parë të 30 rreshtat e kabinës rozë (1A–30F) dhe zgjidhni vendin tuaj.");

        JButton btnVazhdoRezervimin = new RoundedButton("Konfirmo Ulësen & Vazhdo  >", PINK_DEEP, Color.WHITE, 20);
        btnVazhdoRezervimin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVazhdoRezervimin.setEnabled(false);
        btnVazhdoRezervimin.addActionListener(e -> {
            if (ulesjaEZgjedhur[0] != null) {
                dritarjaAvionit.dispose();
                hapZgjidhPaketenDheHotelin(f, ulesjaEZgjedhur[0]);
            }
        });

        JButton btnZgjidhAutomatikisht = new RoundedButton("🎲 Zgjidh Ulëse Dritareje", ROSE_DARK, Color.WHITE, 20);
        btnZgjidhAutomatikisht.setFont(new Font("Segoe UI", Font.BOLD, 12));

        paneliDjathtas.add(lblTitulliInspector);
        paneliDjathtas.add(Box.createVerticalStrut(12));
        paneliDjathtas.add(lblNumriUleses);
        paneliDjathtas.add(Box.createVerticalStrut(8));
        paneliDjathtas.add(lblPozicioni);
        paneliDjathtas.add(Box.createVerticalStrut(4));
        paneliDjathtas.add(lblKategoria);
        paneliDjathtas.add(Box.createVerticalStrut(10));
        paneliDjathtas.add(lblCmimiUleses);
        paneliDjathtas.add(Box.createVerticalStrut(14));
        paneliDjathtas.add(pershkrimiUleses);
        paneliDjathtas.add(Box.createVerticalStrut(18));
        paneliDjathtas.add(btnVazhdoRezervimin);
        paneliDjathtas.add(Box.createVerticalStrut(10));
        paneliDjathtas.add(btnZgjidhAutomatikisht);

        dritarjaAvionit.add(paneliDjathtas, BorderLayout.EAST);

        // Interiori i aeroplanit me gradient Pink Ombré
        JPanel trupiAeroplanit = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int cx = w / 2;

                // Sfondi jashtë avionit Pink Pastel Ombré
                g2.setPaint(new GradientPaint(0, 0, new Color(252, 228, 240), 0, h, new Color(243, 195, 220)));
                g2.fillRect(0, 0, w, h);

                int yKrahet = (int) (h * 0.36);
                g2.setColor(new Color(235, 180, 208));
                g2.fillPolygon(new Polygon(new int[]{cx - 190, 8, 8, cx - 190}, new int[]{yKrahet, yKrahet + 160, yKrahet + 280, yKrahet + 200}, 4));
                g2.fillPolygon(new Polygon(new int[]{cx + 190, w - 8, w - 8, cx + 190}, new int[]{yKrahet, yKrahet + 160, yKrahet + 280, yKrahet + 200}, 4));
                g2.setColor(PINK_DEEP);
                g2.fillRect(8, yKrahet + 160, 10, 120);
                g2.fillRect(w - 18, yKrahet + 160, 10, 120);

                // Trupi i kabinës brenda me Ombré nga e bardhë-rozë në rozë të butë
                g2.setPaint(new GradientPaint(cx, 15, Color.WHITE, cx, h - 45, new Color(255, 224, 240)));
                g2.fillRoundRect(cx - 200, 15, 400, h - 45, 360, 320);
                g2.setColor(PINK_MID);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(cx - 200, 15, 400, h - 45, 360, 320);

                g2.setColor(new Color(245, 190, 218));
                g2.fillArc(cx - 95, 35, 190, 65, 20, 140);
                g2.setColor(ROSE_DARK);
                g2.drawArc(cx - 95, 35, 190, 65, 20, 140);
            }
        };

        JPanel kornizaKabinës = new JPanel();
        kornizaKabinës.setOpaque(false);
        kornizaKabinës.setLayout(new BoxLayout(kornizaKabinës, BoxLayout.Y_AXIS));
        kornizaKabinës.setBorder(new EmptyBorder(95, 0, 75, 0));

        JPanel zonaSherbimeveLart = new JPanel(new GridLayout(1, 2, 16, 0));
        zonaSherbimeveLart.setOpaque(false);
        zonaSherbimeveLart.setMaximumSize(new Dimension(320, 34));

        JLabel lblWCLart = new JLabel("🚻 WC", SwingConstants.CENTER);
        lblWCLart.setOpaque(true);
        lblWCLart.setBackground(PINK_SOFT);
        lblWCLart.setForeground(ROSE_DARK);
        lblWCLart.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel lblGalleyLart = new JLabel("🍸 PINK LOUNGE BAR", SwingConstants.CENTER);
        lblGalleyLart.setOpaque(true);
        lblGalleyLart.setBackground(PINK_SOFT);
        lblGalleyLart.setForeground(ROSE_DARK);
        lblGalleyLart.setFont(new Font("Segoe UI", Font.BOLD, 12));

        zonaSherbimeveLart.add(lblWCLart);
        zonaSherbimeveLart.add(lblGalleyLart);
        kornizaKabinës.add(zonaSherbimeveLart);
        kornizaKabinës.add(Box.createVerticalStrut(10));

        JPanel rreshtiTitujve = new JPanel(new GridLayout(1, 7, 4, 0));
        rreshtiTitujve.setOpaque(false);
        rreshtiTitujve.setMaximumSize(new Dimension(345, 24));
        for (String s : new String[]{"A", "B", "C", " ", "D", "E", "F"}) {
            JLabel lbl = new JLabel(s, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(ROSE_DARK);
            rreshtiTitujve.add(lbl);
        }
        kornizaKabinës.add(rreshtiTitujve);
        kornizaKabinës.add(Box.createVerticalStrut(5));

        ArrayList<Ulesja> uleset = f.getUleset();
        ArrayList<JButton> teGjithaButonat = new ArrayList<>();
        String[] shkronjatKol = {"A", "B", "C", "D", "E", "F"};

        for (int i = 0; i < uleset.size(); i += 6) {
            int nrRreshtit = (i / 6) + 1;

            if (nrRreshtit == 4) {
                JLabel lblEcon = new JLabel("———   PINK ECONOMY CABIN   ———", SwingConstants.CENTER);
                lblEcon.setFont(new Font("Segoe UI", Font.BOLD, 10));
                lblEcon.setForeground(PINK_DEEP);
                lblEcon.setAlignmentX(Component.CENTER_ALIGNMENT);
                kornizaKabinës.add(lblEcon);
                kornizaKabinës.add(Box.createVerticalStrut(4));
            }

            JPanel rreshtUlesesh = new JPanel(new GridLayout(1, 7, 4, 4));
            rreshtUlesesh.setOpaque(false);
            rreshtUlesesh.setMaximumSize(new Dimension(345, 34));

            for (int kol = 0; kol < 7; kol++) {
                if (kol == 3) {
                    JLabel korridori = new JLabel(String.valueOf(nrRreshtit), SwingConstants.CENTER);
                    korridori.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    korridori.setForeground(ROSE_DARK);
                    rreshtUlesesh.add(korridori);
                } else {
                    int idxNeRresht = (kol < 3) ? kol : kol - 1;
                    int idxUleses = i + idxNeRresht;
                    if (idxUleses < uleset.size()) {
                        Ulesja u = uleset.get(idxUleses);
                        String shkronja = shkronjatKol[idxNeRresht];
                        String etiketaVizuale = nrRreshtit + shkronja;

                        JButton btn = new JButton(etiketaVizuale);
                        btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
                        btn.setMargin(new Insets(1, 1, 1, 1));
                        btn.setFocusPainted(false);
                        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

                        if (u.isEshteEZene()) {
                            btn.setBackground(E_KUQE_ZENE);
                            btn.setForeground(Color.WHITE);
                            btn.setEnabled(false);
                        } else if (u.getKlasa().equalsIgnoreCase("Business") || nrRreshtit <= 3) {
                            btn.setBackground(ROSE_GOLD);
                            btn.setForeground(ROSE_DARK);
                        } else if (nrRreshtit == 12 || nrRreshtit == 13 || nrRreshtit == 30) {
                            btn.setBackground(new Color(255, 153, 204));
                            btn.setForeground(ROSE_DARK);
                        } else {
                            btn.setBackground(new Color(255, 224, 239));
                            btn.setForeground(ROSE_DARK);
                        }

                        if (!u.isEshteEZene()) {
                            teGjithaButonat.add(btn);
                            btn.addActionListener(e -> {
                                ulesjaEZgjedhur[0] = u;
                                lblNumriUleses.setText("Ulësja: " + etiketaVizuale);
                                String poz = (shkronja.equals("A") || shkronja.equals("F")) ? "🪟 Dritare (Window)"
                                           : (shkronja.equals("C") || shkronja.equals("D")) ? "🚶 Korridor (Aisle)"
                                           : "💺 Në mes (Middle)";
                                lblPozicioni.setText("Pozicioni: " + poz);
                                String kat = (nrRreshtit <= 3) ? "Business Class"
                                           : (nrRreshtit == 12 || nrRreshtit == 13 || nrRreshtit == 30) ? "Extra Legroom"
                                           : "Economy Class";
                                lblKategoria.setText("Kategoria: " + kat);
                                double cmimi = f.getCmimiDinamik() + (nrRreshtit <= 3 ? 45 : (kat.equals("Extra Legroom") ? 15 : 0));
                                lblCmimiUleses.setText("Çmimi total: " + String.format("%.2f", cmimi) + " EUR");
                                pershkrimiUleses.setText("Ulësja " + etiketaVizuale + " është e lirë!\n• " + poz + "\n• Rreshti " + nrRreshtit + " (" + kat + ")");
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

        btnZgjidhAutomatikisht.addActionListener(e -> {
            for (JButton b : teGjithaButonat) {
                if (b.getText().endsWith("A") || b.getText().endsWith("F")) {
                    b.doClick();
                    return;
                }
            }
            if (!teGjithaButonat.isEmpty()) teGjithaButonat.get(0).doClick();
        });

        trupiAeroplanit.add(kornizaKabinës, BorderLayout.CENTER);
        JScrollPane scrollKabina = new JScrollPane(trupiAeroplanit);
        scrollKabina.getVerticalScrollBar().setUnitIncrement(16);
        dritarjaAvionit.add(scrollKabina, BorderLayout.CENTER);
        dritarjaAvionit.setVisible(true);
    }

    // ================= HAPI 2/3: TARIFA + HOTELI ME YJE (DERI 5★ & 6+ HOTELE PËR DESTINACION) =================
    private void hapZgjidhPaketenDheHotelin(Fluturimi f, Ulesja ulesja) {
        JDialog dritarjaPaketa = new JDialog(this, "COPA AIR  |  Hapi 2/3: Tarifa, Bagazhi & Hotelet sipas Yjeve", true);
        dritarjaPaketa.setSize(850, 680);
        dritarjaPaketa.setLocationRelativeTo(this);
        dritarjaPaketa.setLayout(new BorderLayout());
        dritarjaPaketa.getContentPane().setBackground(PINK_BG);

        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, ROSE_DARK, getWidth(), 0, PINK_DEEP));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel lblTitulli = new JLabel("<html><font color='#F5C26B' size='4'><b>🌸 ZGJIDHNI TARIFËN & HOTELIN NË " + f.getMberritja().getQyteti().toUpperCase() + "</b></font><br>"
                + "<font color='white'>Fluturimi <b>" + f.getNumriFluturimit() + "</b> (" + f.getNisja().getKodiIATA() + " ➔ " + f.getMberritja().getKodiIATA() + ")  •  "
                + "Ulësja: <b>" + ulesja.getNumriUleses() + "</b></font></html>");
        header.add(lblTitulli, BorderLayout.WEST);
        dritarjaPaketa.add(header, BorderLayout.NORTH);

        JPanel qendra = new JPanel();
        qendra.setLayout(new BoxLayout(qendra, BoxLayout.Y_AXIS));
        qendra.setBackground(PINK_BG);
        qendra.setBorder(new EmptyBorder(16, 24, 14, 24));

        JPanel kartelatPanel = new JPanel(new GridLayout(1, 3, 14, 0));
        kartelatPanel.setOpaque(false);
        kartelatPanel.setMaximumSize(new Dimension(790, 140));

        final double[] kostoPaketa = {0.0};
        final String[] emriPaketes = {"Economy Basic"};

        RoundedPanel cardBasic = krijoKarteleTarife("ECONOMY BASIC", "0.00 €", "✔ 1 Çantë dore (8kg)\n✔ Vend standard\n✖ Pa valixhe të regjistruar", true);
        RoundedPanel cardClassic = krijoKarteleTarife("ECONOMY CLASSIC", "+25.00 €", "✔ Valixhe 23kg e përfshirë\n✔ Zgjedhje falas e ulëses\n✔ Ndryshim bilete i lehtë", false);
        RoundedPanel cardFull = krijoKarteleTarife("PINK VIP FULL", "+50.00 €", "✔ 2 Valixhe (deri 32kg)\n✔ Priority Boarding\n✔ Rimbursim 100% falas", false);

        kartelatPanel.add(cardBasic);
        kartelatPanel.add(cardClassic);
        kartelatPanel.add(cardFull);
        qendra.add(kartelatPanel);
        qendra.add(Box.createVerticalStrut(12));

        // Bagazhi
        RoundedPanel bagazhPanel = new RoundedPanel(18, Color.WHITE, new Color(235, 195, 215));
        bagazhPanel.setLayout(new BorderLayout(10, 4));
        bagazhPanel.setMaximumSize(new Dimension(790, 66));
        bagazhPanel.setBorder(new EmptyBorder(8, 16, 8, 16));

        JLabel lblBagazhi = new JLabel("🧳 Pesha e Bagazhit të Regjistruar: 15 kg");
        lblBagazhi.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBagazhi.setForeground(ROSE_DARK);

        JSlider sliderBagazhi = new JSlider(0, 32, 15);
        sliderBagazhi.setBackground(Color.WHITE);
        sliderBagazhi.setMajorTickSpacing(8);
        sliderBagazhi.setPaintTicks(true);
        sliderBagazhi.setPaintLabels(true);

        bagazhPanel.add(lblBagazhi, BorderLayout.NORTH);
        bagazhPanel.add(sliderBagazhi, BorderLayout.CENTER);
        qendra.add(bagazhPanel);
        qendra.add(Box.createVerticalStrut(12));

        // Seksioni i Hoteleve me zgjedhjen e YJEVE (3★, 4★, 5★) dhe 6+ Hotele për çdo destinacion!
        RoundedPanel hotelPanel = new RoundedPanel(20, Color.WHITE, PINK_MID);
        hotelPanel.setLayout(new GridLayout(3, 2, 12, 8));
        hotelPanel.setMaximumSize(new Dimension(790, 145));
        hotelPanel.setBorder(new EmptyBorder(10, 16, 10, 16));

        JComboBox<String> comboYjet = new JComboBox<>(new String[]{
            "Të gjitha kategoritë (3★ - 5★)",
            "⭐⭐⭐⭐⭐  5 Yje (Luxury & Spa)",
            "⭐⭐⭐⭐  4 Yje (Superior Boutique)",
            "⭐⭐⭐  3 Yje (City Comfort)"
        });
        comboYjet.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JComboBox<String> comboHoteli = new JComboBox<>();
        comboHoteli.setFont(new Font("Segoe UI", Font.BOLD, 12));

        String qyteti = f.getMberritja().getQyteti();
        // Gjeneron 6 hotele reale/të dedikuara për destinacionin e zgjedhur sipas yjeve
        Runnable ngarkoHotelet = () -> {
            comboHoteli.removeAllItems();
            comboHoteli.addItem("Pa hotel (Vetëm fluturimi) - 0 €/natë");
            int zgjedhjaYje = comboYjet.getSelectedIndex();
            if (zgjedhjaYje == 0 || zgjedhjaYje == 1) {
                comboHoteli.addItem("⭐⭐⭐⭐⭐ " + qyteti + " Grand Imperial Palace & Spa - 145 €/natë");
                comboHoteli.addItem("⭐⭐⭐⭐⭐ The Ritz-Rose Luxury Suites " + qyteti + " - 165 €/natë");
            }
            if (zgjedhjaYje == 0 || zgjedhjaYje == 2) {
                comboHoteli.addItem("⭐⭐⭐⭐ " + qyteti + " Boutique Skyline Hotel - 85 €/natë");
                comboHoteli.addItem("⭐⭐⭐⭐ Copa Plaza Central " + qyteti + " - 92 €/natë");
            }
            if (zgjedhjaYje == 0 || zgjedhjaYje == 3) {
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
        hotelPanel.add(new JLabel("🌙 Numri i netëve të qëndrimit:"));
        hotelPanel.add(spinNetet);

        qendra.add(hotelPanel);
        qendra.add(Box.createVerticalStrut(12));

        // Shërbimet shtesë
        JPanel addonsPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        addonsPanel.setOpaque(false);
        addonsPanel.setMaximumSize(new Dimension(790, 58));

        JCheckBox chkPriority = stilezoAddon("⚡ Priority Boarding", "+10.00 €");
        JCheckBox chkUshqim = stilezoAddon("🥂 Pink Champagne & Menu", "+12.00 €");
        JCheckBox chkSigurim = stilezoAddon("🛡 Sigurim Udhëtimi", "+8.00 €");

        addonsPanel.add(chkPriority);
        addonsPanel.add(chkUshqim);
        addonsPanel.add(chkSigurim);
        qendra.add(addonsPanel);

        dritarjaPaketa.add(qendra, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(12, 24, 12, 24));

        double cmimiBaze = f.getCmimiDinamik();
        JLabel lblTotali = new JLabel("Totali: " + String.format("%.2f", cmimiBaze) + " EUR");
        lblTotali.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTotali.setForeground(ROSE_DARK);

        Runnable perditesoTotalin = () -> {
            String sel = comboHoteli.getSelectedItem() != null ? comboHoteli.getSelectedItem().toString() : "";
            double hCmim = 0;
            if (sel.contains("145 €")) hCmim = 145;
            else if (sel.contains("165 €")) hCmim = 165;
            else if (sel.contains("85 €")) hCmim = 85;
            else if (sel.contains("92 €")) hCmim = 92;
            else if (sel.contains("52 €")) hCmim = 52;
            else if (sel.contains("45 €")) hCmim = 45;

            int nete = (int) spinNetet.getValue();
            double shtese = kostoPaketa[0] + (hCmim * nete);
            if (chkPriority.isSelected()) shtese += 10;
            if (chkUshqim.isSelected()) shtese += 12;
            if (chkSigurim.isSelected()) shtese += 8;
            double totali = cmimiBaze + shtese;
            lblTotali.setText("<html>Totali (Fluturim + Hotel): <font color='#B8256F'>" + String.format("%.2f", totali) + " EUR</font></html>");
        };

        comboYjet.addActionListener(e -> {
            ngarkoHotelet.run();
            perditesoTotalin.run();
        });
        comboHoteli.addActionListener(e -> perditesoTotalin.run());
        spinNetet.addChangeListener(e -> perditesoTotalin.run());
        chkPriority.addActionListener(e -> perditesoTotalin.run());
        chkUshqim.addActionListener(e -> perditesoTotalin.run());
        chkSigurim.addActionListener(e -> perditesoTotalin.run());

        RoundedPanel[] kartelat = {cardBasic, cardClassic, cardFull};
        double[] cmimetKartelave = {0.0, 25.0, 50.0};
        String[] emratKartelave = {"Economy Basic", "Economy Classic", "Pink VIP Full"};

        for (int i = 0; i < 3; i++) {
            final int idx = i;
            kartelat[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    kostoPaketa[0] = cmimetKartelave[idx];
                    emriPaketes[0] = emratKartelave[idx];
                    for (int j = 0; j < 3; j++) {
                        kartelat[j].setBorderColor(j == idx ? PINK_DEEP : new Color(235, 195, 215));
                        kartelat[j].setBackground(j == idx ? PINK_SOFT : Color.WHITE);
                    }
                    perditesoTotalin.run();
                }
            });
        }

        sliderBagazhi.addChangeListener(e -> lblBagazhi.setText("🧳 Pesha e Bagazhit të Regjistruar: " + sliderBagazhi.getValue() + " kg"));

        JPanel butonaFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        butonaFooter.setOpaque(false);

        JButton btnAnulo = new RoundedButton("Anulo", new Color(235, 225, 230), ROSE_DARK, 18);
        btnAnulo.addActionListener(e -> dritarjaPaketa.dispose());

        JButton btnVazhdo = new RoundedButton("Vazhdo te Pagesa & Pasagjeri  >", PINK_DEEP, Color.WHITE, 20);
        btnVazhdo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVazhdo.setPreferredSize(new Dimension(250, 42));
        btnVazhdo.addActionListener(e -> {
            String sel = comboHoteli.getSelectedItem() != null ? comboHoteli.getSelectedItem().toString() : "";
            double hCmim = sel.contains("145 €") ? 145 : sel.contains("165 €") ? 165 : sel.contains("85 €") ? 85 : sel.contains("92 €") ? 92 : sel.contains("52 €") ? 52 : sel.contains("45 €") ? 45 : 0;
            int nete = (int) spinNetet.getValue();
            double kostoShtese = kostoPaketa[0] + (hCmim * nete);

            StringBuilder sherbimet = new StringBuilder();
            if (chkPriority.isSelected()) { kostoShtese += 10; sherbimet.append("Priority, "); }
            if (chkUshqim.isSelected()) { kostoShtese += 12; sherbimet.append("Menu VIP, "); }
            if (chkSigurim.isSelected()) { kostoShtese += 8; sherbimet.append("Sigurim, "); }

            String paketaFinal = emriPaketes[0];
            if (hCmim > 0) {
                String emriHotelit = sel.split("-")[0].trim();
                paketaFinal += " + " + emriHotelit + " (" + nete + " netë)";
            }
            String sherbimetStr = sherbimet.length() > 0 ? sherbimet.substring(0, sherbimet.length() - 2) : "Standard";

            dritarjaPaketa.dispose();
            hapDritarenEPagesesDhePasagjerit(f, ulesja, sliderBagazhi.getValue(), paketaFinal, sherbimetStr, kostoShtese);
        });

        butonaFooter.add(btnAnulo);
        butonaFooter.add(btnVazhdo);

        footer.add(lblTotali, BorderLayout.WEST);
        footer.add(butonaFooter, BorderLayout.EAST);
        dritarjaPaketa.add(footer, BorderLayout.SOUTH);

        dritarjaPaketa.setVisible(true);
    }

    // ================= HAPI 3/3: DRITARE MODERNE E PAGESËS (ME SHUMË OPSIONE) =================
    private void hapDritarenEPagesesDhePasagjerit(Fluturimi f, Ulesja ulesja, double pesha, String paketa, String sherbimet, double kostoShtese) {
        JDialog dlg = new JDialog(this, "COPA AIR  |  Hapi 3/3: Pasagjeri & Mënyra e Pagesës", true);
        dlg.setSize(620, 560);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(PINK_BG);

        JPanel top = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, ROSE_DARK, getWidth(), 0, PINK_DEEP));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        top.setBorder(new EmptyBorder(14, 22, 14, 22));
        top.add(new JLabel("<html><font color='#F5C26B' size='4'><b>💳 TË DHËNAT E PASAGJERIT & PAGESA</b></font><br>"
                + "<font color='white'>Zgjidhni mënyrën e pagesës dhe finalizoni biletën tuaj reale</font></html>"), BorderLayout.WEST);
        dlg.add(top, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(PINK_BG);
        body.setBorder(new EmptyBorder(16, 24, 16, 24));

        RoundedPanel formCard = new RoundedPanel(20, Color.WHITE, new Color(235, 195, 215));
        formCard.setLayout(new GridLayout(4, 2, 10, 10));
        formCard.setBorder(new EmptyBorder(14, 16, 14, 16));

        JTextField fEmri = new JTextField();
        JTextField fMbiemri = new JTextField();
        JTextField fNrPers = new JTextField();
        JTextField fPasaporta = new JTextField("P01234567");

        formCard.add(new JLabel("👤 Emri i Pasagjerit:"));
        formCard.add(fEmri);
        formCard.add(new JLabel("👤 Mbiemri:"));
        formCard.add(fMbiemri);
        formCard.add(new JLabel("🆔 Numri Personal:"));
        formCard.add(fNrPers);
        formCard.add(new JLabel("🛂 Numri i Pasaportës:"));
        formCard.add(fPasaporta);

        body.add(formCard);
        body.add(Box.createVerticalStrut(14));

        // Kartat e pagesës me shumë opsione atraktive
        RoundedPanel payCard = new RoundedPanel(20, Color.WHITE, PINK_MID);
        payCard.setLayout(new BorderLayout(8, 8));
        payCard.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel lblPay = new JLabel("💳 Zgjidhni Mënyrën e Pagesës:");
        lblPay.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPay.setForeground(ROSE_DARK);
        payCard.add(lblPay, BorderLayout.NORTH);

        JPanel payGrid = new JPanel(new GridLayout(2, 3, 8, 8));
        payGrid.setOpaque(false);

        String[] opsionetPageses = {
            "💳 Visa / Mastercard",
            "🍎 Apple Pay",
            "🤖 Google Pay",
            "🅿 PayPal Express",
            "🏦 Bank Transfer",
            "🌸 Pink VIP Miles"
        };
        ButtonGroup bgPay = new ButtonGroup();
        JRadioButton[] rbs = new JRadioButton[opsionetPageses.length];
        for (int i = 0; i < opsionetPageses.length; i++) {
            rbs[i] = new JRadioButton(opsionetPageses[i], i == 0);
            rbs[i].setBackground(PINK_SOFT);
            rbs[i].setFont(new Font("Segoe UI", Font.BOLD, 12));
            rbs[i].setForeground(ROSE_DARK);
            bgPay.add(rbs[i]);
            payGrid.add(rbs[i]);
        }
        payCard.add(payGrid, BorderLayout.CENTER);
        body.add(payCard);

        dlg.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(Color.WHITE);

        JButton btnMbyll = new RoundedButton("Anulo", new Color(235, 225, 230), ROSE_DARK, 18);
        btnMbyll.addActionListener(e -> dlg.dispose());

        JButton btnPaguaj = new RoundedButton("✔ Paguaj & Gjenero Biletën Reale", PINK_DEEP, Color.WHITE, 20);
        btnPaguaj.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPaguaj.setPreferredSize(new Dimension(270, 42));
        btnPaguaj.addActionListener(e -> {
            if (fEmri.getText().trim().isEmpty() || fMbiemri.getText().trim().isEmpty()) {
                shfaqMesazhModern("Kujdes", "Ju lutem plotësoni emrin dhe mbiemrin e pasagjerit!");
                return;
            }
            String menyra = "Visa / Mastercard";
            for (JRadioButton rb : rbs) {
                if (rb.isSelected()) menyra = rb.getText();
            }
            boolean kaPromo = fushaPromoCode.getText().trim().equalsIgnoreCase("COPA20");
            Pasagjeri p = sistemi.gjejOseKrijoPasagjer(fEmri.getText().trim(), fMbiemri.getText().trim(), fNrPers.getText().trim(), fPasaporta.getText().trim());
            Bileta b = sistemi.rezervoBileteKomplete(p, f, ulesja, pesha, paketa, sherbimet, kostoShtese, menyra, kaPromo);
            if (b != null) {
                b.kryejCheckIn();
                dlg.dispose();
                rifreskoMyTrips();
                cardLayout.show(qendraKardave, "MY_TRIPS");
                shfaqBoardingPassReal(b);
            }
        });

        footer.add(btnMbyll);
        footer.add(btnPaguaj);
        dlg.add(footer, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ================= BOARDING PASS REAL ME BUTON "COPY PNR CODE" =================
    private void shfaqBoardingPassReal(Bileta b) {
        JDialog bp = new JDialog(this, "COPA AIR OFFICIAL BOARDING PASS - " + b.getKodiRezervimit(), true);
        bp.setSize(680, 410);
        bp.setLocationRelativeTo(this);
        bp.setLayout(new BorderLayout());
        bp.getContentPane().setBackground(PINK_BG);

        // Koka e biletës reale (Pink Ombré Gradient)
        JPanel top = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, ROSE_DARK, getWidth(), 0, PINK_DEEP));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        top.setBorder(new EmptyBorder(14, 22, 14, 22));
        JLabel l1 = new JLabel("<html><font color='#F5C26B' size='5'><b>🌸 COPA AIR</b></font> <font color='white' size='4'> — OFFICIAL E-TICKET & BOARDING PASS</font></html>");
        JLabel l2 = new JLabel("CONFIRMED • VIP");
        l2.setForeground(ROSE_GOLD);
        l2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        top.add(l1, BorderLayout.WEST);
        top.add(l2, BorderLayout.EAST);

        // Trupi i biletës me efekt perforation (vijë e ndërprerë) dhe barkod real
        JPanel ticketBody = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(16, 12, w - 32, h - 24, 28, 28);
                g2.setColor(PINK_MID);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(16, 12, w - 32, h - 24, 28, 28);

                // Vija e ndërprerë e biletës (Stub)
                int splitX = (int) (w * 0.70);
                Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6}, 0);
                g2.setStroke(dashed);
                g2.setColor(new Color(220, 170, 195));
                g2.drawLine(splitX, 18, splitX, h - 18);

                // Gjysmë-rrathët anësorë si biletë reale
                g2.setColor(PINK_BG);
                g2.fillOval(splitX - 12, 0, 24, 24);
                g2.fillOval(splitX - 12, h - 24, 24, 24);
            }
        };
        ticketBody.setOpaque(false);
        ticketBody.setBorder(new EmptyBorder(24, 34, 24, 34));

        JPanel majtas = new JPanel(new GridLayout(4, 2, 12, 8));
        majtas.setOpaque(false);
        majtas.add(new JLabel("<html><font color='gray' size='2'>PASSENGER NAME</font><br><b>" + b.getPasagjeri().getEmriPlote().toUpperCase() + "</b></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>FLIGHT & TIME</font><br><b>" + b.getFluturimi().getNumriFluturimit() + " • " + b.getFluturimi().getDataOraNisjes() + "</b></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>FROM</font><br><font size='4' color='#661042'><b>" + b.getFluturimi().getNisja().getQyteti() + " (" + b.getFluturimi().getNisja().getKodiIATA() + ")</b></font></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>TO</font><br><font size='4' color='#B8256F'><b>" + b.getFluturimi().getMberritja().getQyteti() + " (" + b.getFluturimi().getMberritja().getKodiIATA() + ")</b></font></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>GATE</font><br><font size='5' color='#661042'><b>" + b.getFluturimi().getPortaNisjes() + "</b></font></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>SEAT</font><br><font size='5' color='#B8256F'><b>" + b.getUlesja().getNumriUleses() + "</b></font></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>PACKAGE & HOTEL</font><br><b>" + b.getPagesa() + "</b></html>"));
        majtas.add(new JLabel("<html><font color='gray' size='2'>TOTAL PAID</font><br><b>" + String.format("%.2f", b.getPagesa().getShumaTotale()) + " EUR (" + b.getPagesa().getMenyraPageses() + ")</b></html>"));

        // Pjesa e djathtë (Stub me PNR, Barkod dhe Buton Copy)
        JPanel djathtas = new JPanel();
        djathtas.setOpaque(false);
        djathtas.setPreferredSize(new Dimension(165, 220));
        djathtas.setLayout(new BoxLayout(djathtas, BoxLayout.Y_AXIS));

        JLabel lblPnrTitle = new JLabel("BOOKING PNR CODE");
        lblPnrTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblPnrTitle.setForeground(Color.GRAY);
        lblPnrTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPnr = new JLabel(b.getKodiRezervimit());
        lblPnr.setFont(new Font("Consolas", Font.BOLD, 24));
        lblPnr.setForeground(ROSE_DARK);
        lblPnr.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Barkodi grafik i biletës
        JPanel barcode = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                int[] bars = {2, 4, 1, 3, 2, 5, 1, 2, 4, 2, 1, 3, 2, 4, 1, 2, 3, 2};
                int x = 12;
                for (int i = 0; i < bars.length; i++) {
                    if (i % 2 == 0) g.fillRect(x, 4, bars[i], 42);
                    x += bars[i] + 2;
                }
            }
        };
        barcode.setOpaque(false);
        barcode.setMaximumSize(new Dimension(140, 50));

        JButton btnCopyPNR = new RoundedButton("📋 Copy PNR Code", PINK_DEEP, Color.WHITE, 16);
        btnCopyPNR.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnCopyPNR.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCopyPNR.addActionListener(e -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(b.getKodiRezervimit()), null);
            btnCopyPNR.setText("✔ U kopjua: " + b.getKodiRezervimit());
        });

        djathtas.add(Box.createVerticalStrut(10));
        djathtas.add(lblPnrTitle);
        djathtas.add(lblPnr);
        djathtas.add(Box.createVerticalStrut(8));
        djathtas.add(barcode);
        djathtas.add(Box.createVerticalStrut(10));
        djathtas.add(btnCopyPNR);

        ticketBody.add(majtas, BorderLayout.CENTER);
        ticketBody.add(djathtas, BorderLayout.EAST);

        bp.add(top, BorderLayout.NORTH);
        bp.add(ticketBody, BorderLayout.CENTER);
        bp.setVisible(true);
    }

    // ================= DRITARET MODERNE (ZËVENDËSOJNË JOPTIONPANE) =================
    private void shfaqMesazhModern(String titulli, String mesazhi) {
        JDialog d = new JDialog(this, titulli, true);
        d.setSize(450, 230);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(Color.WHITE);

        JPanel head = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, ROSE_DARK, getWidth(), 0, PINK_DEEP));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        head.setBorder(new EmptyBorder(12, 18, 12, 18));
        JLabel lblT = new JLabel("🌸 " + titulli);
        lblT.setForeground(Color.WHITE);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 15));
        head.add(lblT, BorderLayout.WEST);

        JTextArea txt = new JTextArea(mesazhi);
        txt.setEditable(false);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setForeground(ROSE_DARK);
        txt.setBorder(new EmptyBorder(18, 22, 10, 22));

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        foot.setBackground(Color.WHITE);
        JButton ok = new RoundedButton("OK", PINK_DEEP, Color.WHITE, 16);
        ok.setPreferredSize(new Dimension(95, 34));
        ok.addActionListener(e -> d.dispose());
        foot.add(ok);

        d.add(head, BorderLayout.NORTH);
        d.add(txt, BorderLayout.CENTER);
        d.add(foot, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private String shfaqInputModern(String titulli, String pyetja) {
        final String[] rezultati = {null};
        JDialog d = new JDialog(this, titulli, true);
        d.setSize(440, 220);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(Color.WHITE);

        JPanel head = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, ROSE_DARK, getWidth(), 0, PINK_DEEP));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        head.setBorder(new EmptyBorder(12, 18, 12, 18));
        JLabel lblT = new JLabel("🌸 " + titulli);
        lblT.setForeground(Color.WHITE);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 14));
        head.add(lblT, BorderLayout.WEST);

        JPanel center = new JPanel(new GridLayout(2, 1, 6, 6));
        center.setBackground(Color.WHITE);
        center.setBorder(new EmptyBorder(14, 22, 8, 22));
        JLabel lblQ = new JLabel(pyetja);
        lblQ.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblQ.setForeground(ROSE_DARK);
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.BOLD, 14));
        center.add(lblQ);
        center.add(tf);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        foot.setBackground(Color.WHITE);
        JButton cancel = new RoundedButton("Anulo", new Color(235, 225, 230), ROSE_DARK, 16);
        JButton ok = new RoundedButton("Vazhdo", PINK_DEEP, Color.WHITE, 16);
        cancel.addActionListener(e -> d.dispose());
        ok.addActionListener(e -> {
            rezultati[0] = tf.getText();
            d.dispose();
        });
        foot.add(cancel);
        foot.add(ok);

        d.add(head, BorderLayout.NORTH);
        d.add(center, BorderLayout.CENTER);
        d.add(foot, BorderLayout.SOUTH);
        d.setVisible(true);
        return rezultati[0];
    }

    private void hapOnlineCheckIn() {
        String pnr = shfaqInputModern("Copa Air Online Check-in", "Shkruani kodin PNR (ose ngjiteni me Ctrl+V):");
        if (pnr == null || pnr.trim().isEmpty()) return;
        Bileta b = sistemi.gjejBileteMePNR(pnr.trim());
        if (b == null) {
            shfaqMesazhModern("PNR nuk u gjet", "Kodi PNR '" + pnr + "' nuk u gjet në sistem!");
            return;
        }
        b.kryejCheckIn();
        shfaqBoardingPassReal(b);
        rifreskoMyTrips();
    }

    private void hapDritarenAnulimit() {
        String pnr = shfaqInputModern("Anulimi i Biletës", "Shkruani Kodin PNR për anulimin e biletës:");
        if (pnr != null && !pnr.trim().isEmpty()) {
            if (sistemi.anuloRezervimin(pnr.trim())) {
                rifreskoMyTrips();
                shfaqMesazhModern("Anulimi u krye", "Bileta me kod " + pnr.toUpperCase() + " u anulua dhe ulësja u lirua!");
            } else {
                shfaqMesazhModern("Gabim", "Kodi PNR '" + pnr + "' nuk u gjet!");
            }
        }
    }

    private void hapDritarenShtoFluturim() {
        JDialog d = new JDialog(this, "Shto Fluturim të Ri Copa Air", true);
        d.setSize(480, 440);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(Color.WHITE);

        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(ROSE_DARK);
        head.setBorder(new EmptyBorder(12, 18, 12, 18));
        JLabel lt = new JLabel("✈ Shto Linjë të Re Fluturimi");
        lt.setForeground(Color.WHITE);
        lt.setFont(new Font("Segoe UI", Font.BOLD, 15));
        head.add(lt, BorderLayout.WEST);

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(16, 20, 16, 20));

        JTextField fNr = new JTextField("CM-808");
        JComboBox<Aeroporti> cNisja = new JComboBox<>(sistemi.getAeroportet().toArray(new Aeroporti[0]));
        JComboBox<Aeroporti> cMberritja = new JComboBox<>(sistemi.getAeroportet().toArray(new Aeroporti[0]));
        cMberritja.setSelectedIndex(4);
        JTextField fData = new JTextField("2026-12-25 | Nisja 10:00 → 12:15");
        JTextField fCmimi = new JTextField("89");
        JTextField fPorta = new JTextField("Gate A5");

        form.add(new JLabel("Kodi i Fluturimit:")); form.add(fNr);
        form.add(new JLabel("Origin:")); form.add(cNisja);
        form.add(new JLabel("Destination:")); form.add(cMberritja);
        form.add(new JLabel("Data & Ora e Nisjes:")); form.add(fData);
        form.add(new JLabel("Çmimi Bazë (EUR):")); form.add(fCmimi);
        form.add(new JLabel("Gate:")); form.add(fPorta);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        foot.setBackground(Color.WHITE);
        JButton btnRuaj = new RoundedButton("✔ Shto Fluturimin", PINK_DEEP, Color.WHITE, 18);
        btnRuaj.addActionListener(e -> {
            try {
                double cmimi = Double.parseDouble(fCmimi.getText().trim());
                Avioni av = sistemi.getFlota().get(0);
                Piloti pi = sistemi.getPilotet().get(0);
                if (sistemi.shtoFluturimTeRi(fNr.getText().trim(), (Aeroporti) cNisja.getSelectedItem(),
                        (Aeroporti) cMberritja.getSelectedItem(), fData.getText().trim(), av, pi, cmimi, fPorta.getText().trim())) {
                    d.dispose();
                    shfaqMesazhModern("Sukses", "Fluturimi " + fNr.getText() + " u shtua me sukses!");
                }
            } catch (NumberFormatException ex) {
                shfaqMesazhModern("Gabim", "Çmimi duhet të jetë numër!");
            }
        });
        foot.add(btnRuaj);

        d.add(head, BorderLayout.NORTH);
        d.add(form, BorderLayout.CENTER);
        d.add(foot, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private RoundedPanel krijoKarteleTarife(String titulli, String cmimi, String detajet, boolean zgjedhur) {
        RoundedPanel card = new RoundedPanel(20, zgjedhur ? PINK_SOFT : Color.WHITE, zgjedhur ? PINK_DEEP : new Color(235, 195, 215));
        card.setLayout(new BorderLayout(6, 8));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel lblTop = new JLabel("<html><font color='#661042' size='3'><b>" + titulli + "</b></font><br>"
                + "<font color='#B8256F' size='5'><b>" + cmimi + "</b></font></html>");

        JTextArea txt = new JTextArea(detajet);
        txt.setEditable(false);
        txt.setOpaque(false);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txt.setForeground(Color.DARK_GRAY);

        card.add(lblTop, BorderLayout.NORTH);
        card.add(txt, BorderLayout.CENTER);
        return card;
    }

    private JCheckBox stilezoAddon(String emri, String cmimi) {
        JCheckBox chk = new JCheckBox("<html><b>" + emri + "</b><br><font color='#B8256F'>" + cmimi + "</font></html>");
        chk.setBackground(Color.WHITE);
        chk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chk.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(235, 195, 215)),
            new EmptyBorder(6, 10, 6, 10)
        ));
        chk.setBorderPainted(true);
        return chk;
    }

    private JButton krijoLinkNav(String teksti) {
        JButton btn = new JButton(teksti);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ================= KLASA NDIHMËSE PËR KËNDE TË RRUMBULLAKOSURA (ROUNDED EDGES) =================
    static class RoundedPanel extends JPanel {
        private int radius;
        private Color borderColor;

        public RoundedPanel(int radius, Color bgColor, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
            setBackground(bgColor);
            setOpaque(false);
        }

        public void setBorderColor(Color c) {
            this.borderColor = c;
            repaint();
        }

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
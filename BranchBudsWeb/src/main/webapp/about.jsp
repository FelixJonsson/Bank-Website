<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>About BranchBuds</title>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f3f5f7;
            color: #1f2933;
        }
        a {
            color: #1f5f8b;
            text-decoration: none;
        }
        .page {
            max-width: 1120px;
            margin: 0 auto;
            padding: 24px 20px 40px;
        }
        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            margin-bottom: 24px;
            padding-bottom: 16px;
            border-bottom: 1px solid #d8dee4;
        }
        .brand h1 {
            margin: 0 0 4px;
            font-size: 28px;
        }
        .brand p {
            margin: 0;
            color: #52606d;
            font-size: 14px;
        }
        .nav-links {
            display: flex;
            gap: 14px;
            font-size: 14px;
        }
        .nav-links a {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 88px;
            padding: 10px 16px;
            border-radius: 999px;
            background: #111827;
            color: #ffffff;
            font-weight: 600;
        }
        .nav-links a:hover {
            background: #1f2937;
        }
        .panel {
            background: #ffffff;
            border: 1px solid #d8dee4;
            border-radius: 8px;
            padding: 24px;
        }
        .panel h2 {
            margin: 0 0 16px;
            font-size: 24px;
        }
        .about-copy {
            max-width: 760px;
            margin: 0 0 24px;
            color: #52606d;
            line-height: 1.7;
        }
        .founder {
            display: grid;
            grid-template-columns: 220px 1fr;
            gap: 20px;
            align-items: center;
            margin-top: 8px;
        }
        .founder img {
            width: 100%;
            display: block;
            border-radius: 8px;
            border: 1px solid #d8dee4;
        }
        .founder-copy h3 {
            margin: 0 0 8px;
            font-size: 20px;
        }
        .founder-copy p {
            margin: 0;
            color: #52606d;
            line-height: 1.7;
        }
        footer {
            margin-top: 24px;
            text-align: center;
            color: #7b8794;
            font-size: 13px;
        }
        @media (max-width: 800px) {
            .topbar {
                flex-direction: column;
                align-items: stretch;
            }
            .founder {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="page">
        <header class="topbar">
            <div class="brand">
                <h1>BranchBuds</h1>
                <p>Översikt över användare, konton och transaktioner</p>
            </div>
            <nav class="nav-links">
                <a href="${pageContext.request.contextPath}/MainViewServlet">Hem</a>
                <a href="${pageContext.request.contextPath}/about.jsp">Om</a>
            </nav>
        </header>

        <main>
            <section class="panel">
                <h2>Om BranchBuds</h2>
                <p class="about-copy">
                    BranchBuds är ett enkelt budgetverktyg som samlar användare, konton och transaktioner i ett tydligt gränssnitt. Målet är att göra det lätt att få överblick över ekonomin och enkelt att arbeta vidare med transaktioner i samma flöde.
                </p>
                <div class="founder">
                    <img src="founder-preview.png" alt="Grundaren av BranchBuds">
                    <div class="founder-copy">
                        <h3>Grundaren</h3>
                        <p>Det här är grundaren. Han är locked in.</p>
                    </div>
                </div>
            </section>
        </main>

        <footer>
            <p>BranchBuds</p>
        </footer>
    </div>
</body>
</html>

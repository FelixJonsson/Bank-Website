<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>About BranchBuds</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="page">
        <header class="topbar">
            <div class="brand">
                <img src="${pageContext.request.contextPath}/images/branchbuds-logo.png"
                    alt="BranchBuds"
                    class="brand-logo">
                <p>Personal financial overview</p>
            </div>
            <nav class="nav-links">
                <a href="${pageContext.request.contextPath}/MainViewServlet">Home</a>
                <a href="${pageContext.request.contextPath}/about.jsp">About</a>
            </nav>
        </header>

        <main>
            <section class="panel">
                <h2>About BranchBuds</h2>
                <p class="about-copy">
                    BranchBuds is a simple budgeting tool that brings users, accounts and transactions together in one clear interface. The goal is to make it easy to understand your finances and continue working with transactions in the same flow.
                </p>
                <div class="founder">
                    <img src="${pageContext.request.contextPath}/founder-preview.png" alt="Founder of BranchBuds">
                    <div class="founder-copy">
                        <h3>Founder</h3>
                        <p>This is the founder. He is locked in.</p>
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

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
            <a class="brand brand-link" href="${pageContext.request.contextPath}/MainViewServlet">
	<img src="${pageContext.request.contextPath}/images/branchbuds-logo.png"
		alt="BranchBuds"
		class="brand-logo">
	<p>Personal financial overview</p>
</a>
            <nav class="nav-links">
                <a href="${pageContext.request.contextPath}/MainViewServlet">Home</a>
                <a href="${pageContext.request.contextPath}/about.jsp">About</a>
            </nav>
        </header>

        <main>
            <section class="panel about-panel">
	<h2>About BranchBuds</h2>
	<h3>Meet the team!</h3>

	<img src="${pageContext.request.contextPath}/images/team.png"
		alt="BranchBuds team"
		class="team-image">

	<p class="about-copy">
		BranchBuds is a simple budgeting tool that brings users, accounts and transactions together in one clear interface. The goal is to make it easy to understand your finances, track spending patterns and continue working with transactions in the same flow.
	</p>
</section>

        </main>

        <footer>
	<p>BranchBuds · <em>Track spending. Spot patterns. Grow your budget.</em> · 2026</p>
</footer>
    </div>
</body>
</html>

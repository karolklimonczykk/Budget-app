import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Routes, Route, useParams, useNavigate } from "react-router-dom";

import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import TestApiComponent from "../components/TestApiComponent";
import Navbar from "../components/Navbar/Navbar";
import TransactionForm from "../components/TransactionForm/TransactionForm";
import TransactionList from "../components/TransactionList/TransactionList";
import RegisterPage from "../pages/RegisterPage/RegisterPage";
import LoginPage from "../pages/LoginPage/LoginPage";
import HomePage from "../components/HomePage/HomePage";
import PrivateRoute from "../components/Route/PrivateRoute";
import PublicRoute from "../components/Route/PublicRoute";
import BalanceBar from "../components/BalanceBar/BalanceBar";
import GroupMembersPage from "../pages/GroupsPage/GroupMembersPage";
import GroupDebtsPage from "../pages/GroupsPage/GroupDebtPage";
import GroupsPage from "../pages/GroupsPage/GroupsPage";
import { groupsApi } from "../api/groupsApi";

interface Group {
  id: number;
  name: string;
  ownerId: number;
}

const GroupMembersPageWrapper: React.FC = () => {
  const { groupId } = useParams();
  const navigate = useNavigate();
  const [group, setGroup] = useState<Group | null>(null);

  useEffect(() => {
    const fetchGroup = async () => {
      if (!groupId) return;
      try {
        const data = await groupsApi.getGroupById(Number(groupId));
        setGroup(data);
      } catch (error) {
        console.error("Błąd pobierania grupy:", error);
      }
    };
    fetchGroup();
  }, [groupId]);

  if (!group) return <div>Ładowanie grupy...</div>;

  return (
    <GroupMembersPage
      group={group}
      onBack={() => navigate(-1)}
    />
  );
};

const App: React.FC = () => {
  return (
    <Router>
      <Navbar />
      <BalanceBar />
      <div className="container">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route
            path="/add-transaction"
            element={
              <PrivateRoute>
                <TransactionForm />
              </PrivateRoute>
            }
          />
          <Route
            path="/transactions"
            element={
              <PrivateRoute>
                <TransactionList />
              </PrivateRoute>
            }
          />
          <Route
            path="/groups"
            element={
              <PrivateRoute>
                <GroupsPage />
              </PrivateRoute>
            }
          />
          <Route
            path="/groups/:groupId/members"
            element={
              <PrivateRoute>
                <GroupMembersPageWrapper />
              </PrivateRoute>
            }
          />
          <Route
            path="/groups/:groupId/debts"
            element={
              <PrivateRoute>
                <GroupDebtsPage />
              </PrivateRoute>
            }
          />
          <Route path="/test" element={<TestApiComponent />} />
          <Route
            path="/register"
            element={
              <PublicRoute>
                <RegisterPage />
              </PublicRoute>
            }
          />
          <Route
            path="/login"
            element={
              <PublicRoute>
                <LoginPage />
              </PublicRoute>
            }
          />
        </Routes>
      </div>
      <ToastContainer />
    </Router>
  );
};

export default App;

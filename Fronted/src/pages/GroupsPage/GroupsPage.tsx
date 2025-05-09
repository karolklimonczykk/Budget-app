/* src/pages/GroupsPage/GroupsPage.tsx */
import React, { useEffect, useState } from "react";
import { groupsApi } from "../../api/groupsApi";
import { useAuth } from "../../context/AuthContext";
import styles from "./Group.module.scss";
import GroupMembersPage from "./GroupMembersPage";
import { toast } from "react-toastify";

interface Group {
  id: number;
  name: string;
}

const GroupsPage: React.FC = () => {
  const { user } = useAuth();
  const [groups, setGroups] = useState<Group[]>([]);
  const [newGroupName, setNewGroupName] = useState("");
  const [selectedGroup, setSelectedGroup] = useState<Group | null>(null);

  useEffect(() => {
    fetchGroups();
  }, []);

  const fetchGroups = async () => {
    const data = await groupsApi.getGroups();
    setGroups(data);
  };

  const handleCreateGroup = async () => {
    if (!user || !newGroupName.trim()) return;
    await groupsApi.createGroup(newGroupName);
    setNewGroupName("");
    fetchGroups();
  };

  const handleDeleteGroup = async (groupId: number) => {
    if (!window.confirm("Czy na pewno chcesz usunąć tę grupę?")) return;

    try {
      await groupsApi.deleteGroup(groupId);
      toast.success("✅ Grupa usunięta.");
      fetchGroups();
      setSelectedGroup(null);
    } catch (error) {
      console.error("Błąd usuwania grupy:", error);
      toast.error("❌ Nie udało się usunąć grupy.");
    }
  };

  return (
    <div className={styles.container}>
      <h2>Twoje Grupy</h2>

      <div className={styles.form}>
        <input
          type="text"
          placeholder="Nazwa grupy"
          value={newGroupName}
          onChange={(e) => setNewGroupName(e.target.value)}
        />
        <button onClick={handleCreateGroup}>Utwórz Grupę</button>
      </div>

      <ul className={styles.list}>
        {groups.map((group) => (
          <li
            key={group.id}
            onClick={() => setSelectedGroup(group)}
            className={styles.groupItem}
          >
            {group.name}
            <button
              onClick={() => handleDeleteGroup(group.id)}
              className={styles.deleteButton}
            >
              Usuń
            </button>
          </li>
        ))}
      </ul>

      {selectedGroup && (
        <GroupMembersPage
          group={selectedGroup}
          onBack={() => setSelectedGroup(null)}
        />
      )}
    </div>
  );
};

export default GroupsPage;

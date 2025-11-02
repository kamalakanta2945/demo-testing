import AgentDashboard from '../components/admin/AgentDashboard';
import TicketAssignment from '../components/admin/TicketAssignment';
import AnalyticsChart from '../components/admin/AnalyticsChart';

const AdminDashboard = () => {
    return (
        <div>
            <h1>Admin Dashboard</h1>
            <AgentDashboard />
            <TicketAssignment />
            <AnalyticsChart />
        </div>
    );
};

export default AdminDashboard;

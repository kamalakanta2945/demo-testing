import TicketForm from '../components/user/TicketForm';
import TicketList from '../components/user/TicketList';

const UserDashboard = () => {
    return (
        <div>
            <h1>User Dashboard</h1>
            <TicketForm />
            <TicketList />
        </div>
    );
};

export default UserDashboard;

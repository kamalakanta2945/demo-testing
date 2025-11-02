import { useEffect, useState } from 'react';
import { getAllTickets } from '../../services/ticket';
import { Link } from 'react-router-dom';

const TicketList = () => {
    const [tickets, setTickets] = useState([]);

    useEffect(() => {
        getAllTickets().then((response) => {
            setTickets(response.data);
        });
    }, []);

    return (
        <div>
            <h2>My Tickets</h2>
            <ul>
                {tickets.map((ticket) => (
                    <li key={ticket.id}>
                        <Link to={`/tickets/${ticket.id}`}>{ticket.title}</Link> - {ticket.status}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default TicketList;

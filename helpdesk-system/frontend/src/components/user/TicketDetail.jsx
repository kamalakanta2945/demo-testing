import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getTicketById, addReply } from '../../services/ticket';

const TicketDetail = () => {
    const { id } = useParams();
    const [ticket, setTicket] = useState(null);
    const [reply, setReply] = useState('');

    useEffect(() => {
        getTicketById(id).then((response) => {
            setTicket(response.data);
        });
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();
        addReply(id, { content: reply }).then(() => {
            // Refresh the ticket details
            setReply('');
            getTicketById(id).then((response) => {
                setTicket(response.data);
            });
        });
    };

    if (!ticket) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2>{ticket.title}</h2>
            <p>{ticket.description}</p>
            <p>Status: {ticket.status}</p>
            <p>Department: {ticket.department}</p>
            <p>Assigned to: {ticket.assignedTo}</p>

            <h3>Replies</h3>
            {/* Display replies here */}

            <form onSubmit={handleSubmit}>
                <textarea
                    placeholder="Add a reply"
                    value={reply}
                    onChange={(e) => setReply(e.target.value)}
                />
                <button type="submit">Reply</button>
            </form>
        </div>
    );
};

export default TicketDetail;

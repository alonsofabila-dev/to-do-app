import PropTypes from "prop-types";


function formatTime(seconds) {
    if (seconds < 60 && seconds > 0) return "Less than 1 minute";

    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;

    return hours > 0
        ? `${hours}h ${remainingMinutes}m`
        : `${minutes}m`;
}


export function AveragesCard({ averages }) {
    return (
        <div className="mt-3 flex justify-center">
            <div className="bg-white rounded-lg shadow-md p-6 w-3/4 flex">
                <div className="flex flex-col justify-center items-center w-1/2 text-center">
                    <p className="font-semibold text-xl">Average time to finish tasks:</p>
                    <p className="text-lg mt-2">
                        {averages?.totalAverage !== undefined
                            ? formatTime(averages.totalAverage)
                            : "0m"}
                    </p>
                </div>

                <div className="flex flex-col justify-center items-center w-1/2 text-center">
                    <p className="font-semibold text-xl">Average time to finish tasks by priority:</p>
                    <p className="mt-2">
                        Low: {averages?.lowAverage !== undefined
                        ? formatTime(averages.lowAverage)
                        : "0m"}
                    </p>
                    <p className="mt-2">
                        Medium: {averages?.mediumAverage !== undefined
                        ? formatTime(averages.mediumAverage)
                        : "0m"}
                    </p>
                    <p className="mt-2">
                        High: {averages?.highAverage !== undefined
                        ? formatTime(averages.highAverage)
                        : "0m"}
                    </p>
                </div>
            </div>
        </div>
    );
}

AveragesCard.propTypes = {
    averages: PropTypes.shape({
        totalAverage: PropTypes.number,
        lowAverage: PropTypes.number,
        mediumAverage: PropTypes.number,
        highAverage: PropTypes.number,
    }),
};
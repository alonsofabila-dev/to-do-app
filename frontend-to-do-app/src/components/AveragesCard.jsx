import PropTypes from "prop-types";


export function AveragesCard({ averages }) {

    console.log(averages)

    return (
        <div className="mt-3 flex justify-center">
            <div className="bg-white rounded-lg shadow-md p-6 w-3/4 flex">

                <div className="flex flex-col justify-center items-center w-1/2 text-center">
                    <p className="font-semibold text-xl">Average time to finish tasks:</p>
                    <p className="text-lg mt-2">{averages?.totalAverage !== undefined
                        ? averages.totalAverage.toFixed(2)
                        : "0:00"} minutes</p>
                </div>

                <div className="flex flex-col justify-center items-center w-1/2 text-center">
                    <p className="font-semibold text-xl">Average time to finish tasks by priority:</p>
                    <p className="mt-2">Low: {averages?.lowAverage !== undefined
                        ? averages.lowAverage.toFixed(2)
                        : "0:00"} minutes</p>
                    <p className="mt-2">Medium: {averages?.mediumAverage !== undefined
                        ? averages.mediumAverage.toFixed(2)
                        : "0:00"} minutes</p>
                    <p className="mt-2">High: {averages?.highAverage !== undefined
                        ? averages.highAverage.toFixed(2)
                        : "0:00"} minutes</p>
                </div>

            </div>
        </div>
    );
}

AveragesCard.propTypes = {
    averages: PropTypes.object,
}
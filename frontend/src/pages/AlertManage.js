import React, {useEffect, useState} from 'react';
import {getUserAlerts, deleteUserAlert} from '../api/movieApi';
import {useAuth} from '../context/AuthContext';

const AlertManage = () => {
    const {isAuthenticated} = useAuth();
    const [alerts, setAlerts] = useState([]);
    const [loading, setLoading] = useState(true);

    const fetchAlerts = async () => {
        try {
            const data = await getUserAlerts();
            setAlerts(data);
        } catch (error) {
            console.error('알림 목록 불러오기 실패:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (alertId) => {
        if (window.confirm('이 알림을 삭제하시겠습니까?')) {
            try {
                await deleteUserAlert(alertId);
                fetchAlerts();
            } catch (error) {
                console.error('알림 삭제 실패:', error);
                alert('삭제 중 오류가 발생했습니다.');
            }
        }
    };

    useEffect(() => {
        if (isAuthenticated) {
            fetchAlerts();
        }
    }, [isAuthenticated]);

    if (!isAuthenticated) {
        return <div className="text-center py-20 text-lg">로그인이 필요합니다.</div>;
    }

    if (loading) {
        return <div className="text-center py-20 text-gray-500">불러오는 중...</div>;
    }

    if (alerts.length === 0) {
        return <div className="text-center py-20 text-gray-600">등록된 알림이 없습니다.</div>;
    }

    return (
        <div className="max-w-3xl mx-auto px-4 py-12">
            <h1 className="text-3xl font-bold text-center mb-10">🎫 등록한 영화 알림</h1>

            <div className="space-y-6">
                {alerts.map((alert) => (
                    <div
                        key={alert.userMovieAlertId}
                        className="flex w-full bg-white rounded-xl overflow-hidden shadow-sm border"
                    >
                        {/* 왼쪽: 포스터 */}
                        <div className="w-24 h-auto bg-gray-100 flex items-center justify-center">
                            <img
                                src={`https://image.tmdb.org/t/p/w154${alert.posterPath}`}
                                alt={alert.movieTitle}
                                className="w-full object-cover rounded-none"
                            />
                        </div>

                        {/* 중앙: 텍스트 정보 */}
                        <div className="flex-grow px-4 py-3">
                            <h2 className="text-lg font-semibold">{alert.movieTitle}</h2>
                            <p className="text-sm text-gray-500 mt-1">
                                등록일: {new Date(alert.registeredAt).toLocaleDateString()}
                            </p>
                        </div>

                        {/* 오른쪽: 삭제 버튼 */}
                        <div className="flex items-stretch justify-center border-l border-gray-300 w-24">
                            <button
                                onClick={() => handleDelete(alert.userMovieAlertId)}
                                className="bg-red-500 hover:bg-red-600 text-white text-base font-bold w-full h-full transition-all"
                            >
                                삭제
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default AlertManage;
